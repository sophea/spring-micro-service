package com.sma.filter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Locale;

@SuppressWarnings("PMD")
public class TracerRequestFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(TracerRequestFilter.class);

    @Override
    public void destroy() {
        // default constructor
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final StatusExposingServletResponse res = new StatusExposingServletResponse((HttpServletResponse) response);

        final long start = System.currentTimeMillis();
        final String ipAddress = getRemoteAddress(req);
        final String userAgent = req.getHeader("User-Agent");

        LOG.info("start:http:[{}]:{}", req.getMethod(), req.getRequestURI());
        LOG.info(String.format("ip:%s;ua:%s", ipAddress, userAgent));
        try {
            if (LOG.isInfoEnabled()) {
                final MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(req);
                // debug payload info
                logPayLoad(wrappedRequest);
                chain.doFilter(wrappedRequest, res);
            } else {
                chain.doFilter(request, res);
            }
        } finally {
            final long finish = System.currentTimeMillis() - start;
            LOG.info(String.format("ip:[%s]:stop:http:[%s]:[%s]:response-http-status-code:[%s]:%s[ms]", ipAddress, req.getMethod(),
                    req.getRequestURI(), res.getStatus(), finish));
        }
    }

    public static String getRemoteAddress(HttpServletRequest req) {
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        return ipAddress;
    }

    private void logPayLoad(HttpServletRequest request) {
        final StringBuilder params = new StringBuilder(20);
        final String method = request.getMethod();
        final String ipAddress = getRemoteAddress(request);
        final String userAgent = request.getHeader("User-Agent");
        LOG.info("↓↓↓↓↓-Trace_Request_start-↓↓↓↓↓");
        LOG.info(String.format("Access from ip:%s;ua:%s", ipAddress, userAgent));
        LOG.info(String.format("Accept-Language : %s", request.getLocale().toString()));
        LOG.info(String.format("Method : %s requestUri %s", method, request.getRequestURI()));
        params.append("Query Params:").append(System.lineSeparator());
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);

            String paramNameLowerCase = paramName.toLowerCase(Locale.ENGLISH);
            if (paramNameLowerCase.contains("secret") || paramNameLowerCase.contains("password") || "pwd"
                    .equalsIgnoreCase(paramName)) {
                paramValue = "*****";
            }
            params.append("---->").append(paramName).append(":").append(paramValue).append(System.lineSeparator());
        }
        LOG.info(params.toString());
        /** request body */
        // check if multipart/form-data
        if (request.getContentType() != null && request.getContentType().toLowerCase(Locale.ENGLISH).indexOf("multipart/form-data") > -1) {
            LOG.info("content type is file upload");
        } else if ("POST".equals(method) || "PUT".equals(method)) {
            try {
                LOG.info(IOUtils.toString(request.getInputStream()));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // default constructor
    }

    private static final class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
        private ByteArrayOutputStream cachedBytes;

        MultiReadHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (cachedBytes == null) {
                cacheInputStream();
            }
            return new CachedServletInputStream();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), "utf-8"));
        }

        private void cacheInputStream() throws IOException {
            /*
             * Cache the inputstream in order to read it multiple times. For convenience, I use apache.commons IOUtils
             */
            cachedBytes = new ByteArrayOutputStream();
            IOUtils.copy(super.getInputStream(), cachedBytes);
        }

        /* An inputstream which reads the cached request body */
        private final class CachedServletInputStream extends ServletInputStream {
            private final ByteArrayInputStream input;

            private CachedServletInputStream() {
                /* create a new input stream from the cached request body */
                input = new ByteArrayInputStream(cachedBytes.toByteArray());
            }

            @Override
            public int read() throws IOException {
                return input.read();
            }

            @Override
            public boolean isFinished() {
                return input.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                //throw new InternalBusinessException("Not implemented");
            }
        }
    }

    private static final class StatusExposingServletResponse extends HttpServletResponseWrapper {

        private int httpStatus;

        private StatusExposingServletResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int sc) throws IOException {
            httpStatus = sc;
            super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            httpStatus = sc;
            super.sendError(sc, msg);
        }

        @Override
        public void setStatus(int sc, String sm) {
            super.setStatus(sc, sm);
            httpStatus = sc;
        }

        @Override
        public void setStatus(int sc) {
            httpStatus = sc;
            super.setStatus(sc);
        }

        @Override
        public int getStatus() {
            return httpStatus;
        }

    }
}
