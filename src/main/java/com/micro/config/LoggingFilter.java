package com.micro.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger LOG = LogManager.getRootLogger();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();

        if (request.getServletPath().startsWith("/api/v1")) {
            sb.append("\n---- REQUEST ---\n");
            sb.append("URI: ").append(request.getRequestURI()).append("\n");
            sb.append("Method: ").append(request.getMethod()).append("\n");
            sb.append("Headers: ").append(request.getHeaderNames()).append("\n");

            if ((request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT"))
                    && request.getContentLength() > 0) {
                CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
                sb.append("Body: ").append(wrappedRequest.getBody()).append("\n");
                filterChain.doFilter(wrappedRequest, response);
            } else {
                filterChain.doFilter(request, response);
            }
            LOG.info(sb.toString());
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * The purpose of CachedBodyHttpServletRequest is to provide a mechanism to read the HTTP request's
     * body multiple times. In the typical servlet input stream, once the stream has been read to the end,
     * it cannot be read again. This poses a problem for scenarios where we want to log the content of the
     * body, and then later process it in the usual manner (e.g., by binding it to a method parameter).
     *
     * By caching the body of the HTTP request the first time it's read, we can then safely read it multiple
     * times. This class achieves this by wrapping the original HttpServletRequest and then reading and
     * storing the body data in a byte array the first time it's accessed. Subsequent reads will be made
     * against this cached byte array rather than the original stream.
     *
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
}
