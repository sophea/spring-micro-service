package com.sma.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Sophea Mak
 * @since May-2018
 *
 */
public class BasicAuthenticationFilter extends OncePerRequestFilter {

    private static final String BASIC = "BASIC ";
    private String              username = "";
    private String              secret   = "";

    @Override
    public void initFilterBean() throws ServletException {
        final FilterConfig filterConfig = getFilterConfig();
        if (filterConfig != null) {
            username = filterConfig.getInitParameter("username");
            secret = filterConfig.getInitParameter("secret");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        if (checkAuthentication(header, response)) {
            filterChain.doFilter(request, response);
        }

    }

    private boolean checkAuthentication(String authorization, HttpServletResponse response) {

        if (authorization == null || !authorization.toUpperCase(Locale.ENGLISH).startsWith(BASIC)) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Backend API\"");
            response.setStatus(401);
            return false;
        }
        // Get encoded user and password, comes after "BASIC "
        // Decode it, using any base 64 decoder

        final String authValue = StringUtils.toEncodedString(Base64.decodeBase64(authorization.substring(BASIC.length())),
                Charset.defaultCharset());
        final String clientId = getClientUsername(authValue);
        final String pwd = getClientPassword(authValue);

        if (!(username.equals(clientId) && secret.equals(pwd))) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Backend\"");
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private String getClientUsername(final String authValue) {
        String username = authValue;
        final int endIndex = authValue.indexOf(':');
        if (-1 < endIndex) {
            username = authValue.substring(0, endIndex);
        }
        return username;
    }

    private String getClientPassword(final String authValue) {
        String password = authValue;
        final int beginIndex = authValue.indexOf(':');
        if (-1 < beginIndex) {
            password = authValue.substring(beginIndex + 1);
        }
        return password;
    }
}
