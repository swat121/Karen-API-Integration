package com.micro.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger LOG = LogManager.getRootLogger();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        StringBuilder requestLog = new StringBuilder();
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        requestLog.append("\n---- [INCOMING] REQUEST ---\n")
                .append("URI: ").append(request.getRequestURI()).append("\n")
                .append("Method: ").append(request.getMethod()).append("\n")
                .append("Headers: ").append(Collections.list(request.getHeaderNames())).append("\n");

        if (requestNeedsBodyCaching(request)) {
            CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
            requestLog.append("Body: ").append(wrappedRequest.getBody()).append("\n");
            LOG.info(requestLog.toString());
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } else {
            LOG.info(requestLog.toString());
            filterChain.doFilter(request, wrappedResponse);
        }

        StringBuilder responseLog = new StringBuilder();
        byte[] responseBody = wrappedResponse.getContentAsByteArray();
        responseLog.append("\n---- [INCOMING] RESPONSE --- for request ").append(request.getMethod()).append(": ").append(request.getRequestURI()).append("\n")
                .append("Status: ").append(response.getStatus()).append("\n")
                .append("Body: ").append(new String(responseBody, StandardCharsets.UTF_8)).append("\n");
        LOG.info(responseLog.toString());

        wrappedResponse.copyBodyToResponse();
    }

    private boolean requestNeedsBodyCaching(HttpServletRequest request) {
        return (request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT"))
                && request.getContentLength() > 0;
    }

    /**
     * The purpose of CachedBodyHttpServletRequest is to provide a mechanism to read the HTTP request's
     * body multiple times. In the typical servlet input stream, once the stream has been read to the end,
     * it cannot be read again. This poses a problem for scenarios where we want to log the content of the
     * body, and then later process it in the usual manner (e.g., by binding it to a method parameter).
     * <p>
     * By caching the body of the HTTP request the first time it's read, we can then safely read it multiple
     * times. This class achieves this by wrapping the original HttpServletRequest and then reading and
     * storing the body data in a byte array the first time it's accessed. Subsequent reads will be made
     * against this cached byte array rather than the original stream.
     * <p>
     * Note: This approach might introduce some overhead, especially for larger request bodies. It is
     * essential to ensure that this filter is only used for the relevant paths to avoid unnecessary caching
     * and memory usage.
     */
    private static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final byte[] cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            InputStream requestInputStream = request.getInputStream();
            this.cachedBody = toByteArray(requestInputStream);
        }

        @Override
        public ServletInputStream getInputStream() {
            return new CachedServletInputStream(new ByteArrayInputStream(cachedBody));
        }

        public String getBody() {
            return new String(cachedBody, java.nio.charset.StandardCharsets.UTF_8);
        }

        private byte[] toByteArray(InputStream in) throws IOException {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return os.toByteArray();
        }

        private static class CachedServletInputStream extends ServletInputStream {
            private final InputStream cachedBodyInputStream;

            public CachedServletInputStream(InputStream cachedBodyInputStream) {
                this.cachedBodyInputStream = cachedBodyInputStream;
            }

            @Override
            public boolean isFinished() {
                try {
                    return cachedBodyInputStream.available() == 0;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() throws IOException {
                return cachedBodyInputStream.read();
            }
        }
    }

    public static class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream cachedBody = new ByteArrayOutputStream();
        private final ServletOutputStream outputStream = new CachedServletOutputStream(cachedBody);
        private PrintWriter writer;

        public CachedBodyHttpServletResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return outputStream;
        }

        @Override
        public PrintWriter getWriter() {
            if (writer == null) {
                writer = new PrintWriter(outputStream);
            }
            return writer;
        }

        public byte[] getBody() {
            return cachedBody.toByteArray();
        }

        private static class CachedServletOutputStream extends ServletOutputStream {
            private final ByteArrayOutputStream output;

            public CachedServletOutputStream(ByteArrayOutputStream output) {
                this.output = output;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                // not used
            }

            @Override
            public void write(int b) throws IOException {
                output.write(b);
            }
        }
    }
}
