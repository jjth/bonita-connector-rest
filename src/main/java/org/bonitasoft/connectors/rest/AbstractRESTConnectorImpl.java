/**
 * Copyright (C) 2014 BonitaSoft S.A. BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble This
 * library is free software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation version 2.1 of the
 * License. This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.bonitasoft.connectors.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.bonitasoft.connectors.rest.model.AuthorizationType;
import org.bonitasoft.connectors.rest.model.SSLVerifier;
import org.bonitasoft.connectors.rest.model.TrustCertificateStrategy;
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorValidationException;

public abstract class AbstractRESTConnectorImpl extends AbstractConnector {

    protected static final String URL_INPUT_PARAMETER = "url";
    protected static final String METHOD_INPUT_PARAMETER = "method";
    protected static final String CONTENTTYPE_INPUT_PARAMETER = "contentType";
    protected static final String CHARSET_INPUT_PARAMETER = "charset";
    protected static final String URLCOOKIES_INPUT_PARAMETER = "urlCookies";
    protected static final String URLHEADERS_INPUT_PARAMETER = "urlHeaders";
    protected static final String DOCUMENT_BODY_INPUT_PARAMETER = "documentBody";
    protected static final String BODY_INPUT_PARAMETER = "body";
    protected static final String DO_NOT_FOLLOW_REDIRECT_INPUT_PARAMETER = "do_not_follow_redirect";
    protected static final String IGNORE_BODY_INPUT_PARAMETER = "ignore_body";
    protected static final String TRUST_CERTIFICATE_STRATEGY_INPUT_PARAMETER = "trust_strategy";
    protected static final String TLS_INPUT_PARAMETER = "TLS";
    protected static final String HOSTNAME_VERIFIER_INPUT_PARAMETER = "hostname_verifier";
    protected static final String TRUST_STORE_FILE_INPUT_PARAMETER = "trust_store_file";
    protected static final String TRUST_STORE_PASSWORD_INPUT_PARAMETER = "trust_store_password";
    protected static final String KEY_STORE_FILE_INPUT_PARAMETER = "key_store_file";
    protected static final String KEY_STORE_PASSWORD_INPUT_PARAMETER = "key_store_password";
    protected static final String AUTH_TYPE_PARAMETER = "auth_type";
    protected static final String AUTH_USERNAME_INPUT_PARAMETER = "auth_username";
    protected static final String AUTH_PASSWORD_INPUT_PARAMETER = "auth_password";
    protected static final String AUTH_HOST_INPUT_PARAMETER = "auth_host";
    protected static final String AUTH_PORT_INPUT_PARAMETER = "auth_port";
    protected static final String AUTH_REALM_INPUT_PARAMETER = "auth_realm";
    protected static final String AUTH_PREEMPTIVE_INPUT_PARAMETER = "auth_preemptive";
    protected static final String PROXY_PROTOCOL_INPUT_PARAMETER = "proxy_protocol";
    protected static final String PROXY_HOST_INPUT_PARAMETER = "proxy_host";
    protected static final String PROXY_PORT_INPUT_PARAMETER = "proxy_port";
    protected static final String PROXY_USERNAME_INPUT_PARAMETER = "proxy_username";
    protected static final String PROXY_PASSWORD_INPUT_PARAMETER = "proxy_password";
    protected static final String BODY_AS_STRING_OUTPUT_PARAMETER = "bodyAsString";
    protected static final String BODY_AS_OBJECT_OUTPUT_PARAMETER = "bodyAsObject";
    protected static final String HEADERS_OUTPUT_PARAMETER = "headers";
    protected static final String STATUS_CODE_OUTPUT_PARAMETER = "status_code";
    protected static final String STATUS_MESSAGE_OUTPUT_PARAMETER = "status_message";
    protected static final String SOCKET_TIMEOUT_MS_PARAMETER = "socket_timeout_ms";
    protected static final String CONNECTION_TIMEOUT_MS_PARAMETER = "connection_timeout_ms";

    protected static final int SOCKET_TIMEOUT_MS_DEFAULT_VALUE = 60_000;
    protected static final int CONNECTION_TIMEOUT_MS_DEFAULT_VALUE = 60_000;

    protected final java.lang.String getUrl() {
        return (java.lang.String) getInputParameter(URL_INPUT_PARAMETER);
    }

    protected java.lang.String getMethod() {
        return (java.lang.String) getInputParameter(METHOD_INPUT_PARAMETER);
    }

    protected final java.lang.String getContentType() {
        return (java.lang.String) getInputParameter(CONTENTTYPE_INPUT_PARAMETER);
    }

    protected final java.lang.String getCharset() {
        return (java.lang.String) getInputParameter(CHARSET_INPUT_PARAMETER);
    }

    protected final List getUrlCookies() {
        List cookies = (List) getInputParameter(URLCOOKIES_INPUT_PARAMETER);
        if (cookies == null) {
            cookies = Collections.emptyList();
        }
        cookies.removeIf(emptyLines());
        return cookies;
    }

    private Predicate<Object> emptyLines() {
        return new Predicate<Object>() {

            @Override
            public boolean test(Object input) {
                if (input instanceof List) {
                    final List line = (List) input;
                    return line.size() != 2 || (emptyCell(line, 0) && emptyCell(line, 1));
                }
                return true;
            }

            private boolean emptyCell(final List line, int cellIndex) {
                final Object cellValue = line.get(cellIndex);
                return cellValue == null || cellValue.toString().trim().isEmpty();
            }
        };
    }

    protected final List getUrlHeaders() {
        List headers = (List) getInputParameter(URLHEADERS_INPUT_PARAMETER);
        if (headers == null) {
            headers = Collections.emptyList();
        }
        headers.removeIf(emptyLines());
        return headers;
    }

    protected final String getDocumentBody() {
        return (String) getInputParameter(DOCUMENT_BODY_INPUT_PARAMETER);
    }

    protected final String getBody() {
        return (String) getInputParameter(BODY_INPUT_PARAMETER);
    }

    protected final Boolean getTLS() {
        final Boolean tlsParam = (Boolean) getInputParameter(TLS_INPUT_PARAMETER);
        return tlsParam != null ? tlsParam : Boolean.TRUE;
    }

    protected final TrustCertificateStrategy getTrustCertificateStrategy() {
        final String trustParam = (String) getInputParameter(TRUST_CERTIFICATE_STRATEGY_INPUT_PARAMETER);
        if (trustParam != null && !trustParam.trim().isEmpty()) {
            return TrustCertificateStrategy.valueOf(trustParam);
        }
        return TrustCertificateStrategy.DEFAULT;
    }

    protected final SSLVerifier getHostnameVerifier() {
        String hostanmeVerifierParam = (String) getInputParameter(HOSTNAME_VERIFIER_INPUT_PARAMETER);
        if (hostanmeVerifierParam != null && !hostanmeVerifierParam.trim().isEmpty()) {
            return SSLVerifier.getSSLVerifierFromValue(hostanmeVerifierParam.toUpperCase());
        }
        return SSLVerifier.STRICT;
    }

    protected final String getTrustStoreFile() {
        return (String) getInputParameter(TRUST_STORE_FILE_INPUT_PARAMETER);
    }

    protected final String getTrustStorePassword() {
        return (String) getInputParameter(TRUST_STORE_PASSWORD_INPUT_PARAMETER);
    }

    protected final String getKeyStoreFile() {
        return (String) getInputParameter(KEY_STORE_FILE_INPUT_PARAMETER);
    }

    protected final String getKeyStorePassword() {
        return (String) getInputParameter(KEY_STORE_PASSWORD_INPUT_PARAMETER);
    }

    protected final Boolean getDoNotFollowRedirect() {
        final Boolean follozRedirect = (Boolean) getInputParameter(DO_NOT_FOLLOW_REDIRECT_INPUT_PARAMETER);
        return follozRedirect != null ? follozRedirect : Boolean.FALSE;
    }

    protected final Boolean getIgnoreBody() {
        final Boolean ignoreBody = (Boolean) getInputParameter(IGNORE_BODY_INPUT_PARAMETER);
        return ignoreBody != null ? ignoreBody : Boolean.FALSE;
    }

    protected final String getAuthUsername() {
        return (String) getInputParameter(AUTH_USERNAME_INPUT_PARAMETER);
    }

    protected final String getAuthPassword() {
        return (String) getInputParameter(AUTH_PASSWORD_INPUT_PARAMETER);
    }

    protected final String getAuthHost() {
        return (String) getInputParameter(AUTH_HOST_INPUT_PARAMETER);
    }

    protected final Integer getAuthPort() {
        return (Integer) getInputParameter(AUTH_PORT_INPUT_PARAMETER);
    }

    protected final String getAuthRealm() {
        return (String) getInputParameter(AUTH_REALM_INPUT_PARAMETER);
    }

    protected final Boolean getAuthPreemptive() {
        final Boolean preemptive = (Boolean) getInputParameter(AUTH_PREEMPTIVE_INPUT_PARAMETER);
        return preemptive != null ? preemptive : Boolean.TRUE;
    }

    protected final AuthorizationType getAuthType() {
        final String authType = (String) getInputParameter(AUTH_TYPE_PARAMETER);
        return authType != null ? AuthorizationType.valueOf(authType) : AuthorizationType.NONE;
    }

    protected final java.lang.String getProxyProtocol() {
        return (String) getInputParameter(PROXY_PROTOCOL_INPUT_PARAMETER);
    }

    protected final String getProxyHost() {
        return (String) getInputParameter(PROXY_HOST_INPUT_PARAMETER);
    }

    protected final Integer getProxyPort() {
        return (Integer) getInputParameter(PROXY_PORT_INPUT_PARAMETER);
    }

    protected final String getProxyUsername() {
        return (String) getInputParameter(PROXY_USERNAME_INPUT_PARAMETER);
    }

    protected final String getProxyPassword() {
        return (String) getInputParameter(PROXY_PASSWORD_INPUT_PARAMETER);
    }

    protected final Integer getSocketTimeoutMs() {
        Integer socketTimeoutMs = (Integer) getInputParameter(SOCKET_TIMEOUT_MS_PARAMETER);
        return socketTimeoutMs != null ? socketTimeoutMs : SOCKET_TIMEOUT_MS_DEFAULT_VALUE;
    }

    protected final Integer getConnectionTimeoutMs() {
        Integer connectionTimeoutMs = (Integer) getInputParameter(CONNECTION_TIMEOUT_MS_PARAMETER);
        return connectionTimeoutMs != null ? connectionTimeoutMs : CONNECTION_TIMEOUT_MS_DEFAULT_VALUE;
    }

    protected final void setBody(java.lang.String body) {
        setOutputParameter(BODY_AS_STRING_OUTPUT_PARAMETER, body);
    }

    protected final void setBody(Object body) {
        setOutputParameter(BODY_AS_OBJECT_OUTPUT_PARAMETER, body);
    }

    protected final void setHeaders(Map<String, String> headers) {
        setOutputParameter(HEADERS_OUTPUT_PARAMETER, headers);
    }

    protected final void setStatusCode(java.lang.Integer statusCode) {
        setOutputParameter(STATUS_CODE_OUTPUT_PARAMETER, statusCode);
    }

    protected final void setStatusMessage(java.lang.String statusMessage) {
        setOutputParameter(STATUS_MESSAGE_OUTPUT_PARAMETER, statusMessage);
    }

    @Override
    public void validateInputParameters() throws ConnectorValidationException {
        try {
            getUrl();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("url type is invalid");
        }
        try {
            getMethod();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("method type is invalid");
        }
        try {
            getContentType();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("contentType type is invalid");
        }
        try {
            getCharset();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("charset type is invalid");
        }
        try {
            getUrlCookies();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("urlCookies type is invalid");
        }
        try {
            getUrlHeaders();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("urlHeaders type is invalid");
        }
        try {
            getBody();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("body type is invalid");
        }
        try {
            getDoNotFollowRedirect();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("do_not_follow_redirect type is invalid");
        }
        try {
            getIgnoreBody();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("ignore_body type is invalid");
        }
        try {
            getTLS();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("TLS type is invalid");
        }
        validateTrustCertificateStrategyInput();
        validateHostnameVerifierInput();

        try {
            getTrustStoreFile();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("trust_store_file type is invalid");
        }
        try {
            getTrustStorePassword();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("trust_store_password type is invalid");
        }
        try {
            getKeyStoreFile();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("key_store_file type is invalid");
        }
        try {
            getKeyStorePassword();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("key_store_password type is invalid");
        }
        try {
            getAuthUsername();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("auth_basic_username type is invalid");
        }
        try {
            getAuthPassword();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("auth_basic_password type is invalid");
        }
        try {
            getAuthHost();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("auth_basic_host type is invalid");
        }
        try {
            getAuthPort();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("auth_basic_port type is invalid");
        }
        try {
            getAuthRealm();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("auth_basic_realm type is invalid");
        }
        try {
            getAuthPreemptive();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("auth_basic_preemptive type is invalid");
        }
        try {
            getProxyProtocol();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("proxy_protocol type is invalid");
        }
        try {
            getProxyHost();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("proxy_host type is invalid");
        }
        try {
            getProxyPort();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("proxy_port type is invalid");
        }
        try {
            getProxyUsername();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("proxy_username type is invalid");
        }
        try {
            getProxyPassword();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException("proxy_password type is invalid");
        }
        try {
            getSocketTimeoutMs();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException(SOCKET_TIMEOUT_MS_PARAMETER + " type is invalid");
        }
        try {
            getConnectionTimeoutMs();
        } catch (final ClassCastException cce) {
            throw new ConnectorValidationException(CONNECTION_TIMEOUT_MS_PARAMETER + " type is invalid");
        }
    }

    private void validateTrustCertificateStrategyInput() throws ConnectorValidationException {
        String trustParam = null;
        try {
            trustParam = (String) getInputParameter(TRUST_CERTIFICATE_STRATEGY_INPUT_PARAMETER);
            if (trustParam != null && !trustParam.trim().isEmpty()) {
                TrustCertificateStrategy.valueOf(trustParam);
            }
        } catch (ClassCastException cce) {
            throw new ConnectorValidationException(
                    String.format("%s type is invalid", TRUST_CERTIFICATE_STRATEGY_INPUT_PARAMETER));
        } catch (IllegalArgumentException e) {
            throw new ConnectorValidationException(
                    String.format(
                            "'%s' option is invalid for %s. Only one of %s is supported.",
                            trustParam,
                            TRUST_CERTIFICATE_STRATEGY_INPUT_PARAMETER,
                            Arrays.toString(TrustCertificateStrategy.values())));
        }
    }

    private void validateHostnameVerifierInput() throws ConnectorValidationException {
        String hostNameVerifierParam = null;
        try {
            hostNameVerifierParam = (String) getInputParameter(HOSTNAME_VERIFIER_INPUT_PARAMETER);
            if (hostNameVerifierParam != null && !hostNameVerifierParam.trim().isEmpty()) {
                SSLVerifier.getSSLVerifierFromValue(hostNameVerifierParam.toUpperCase());
            }
        } catch (ClassCastException cce) {
            throw new ConnectorValidationException(
                    String.format("%s type is invalid", HOSTNAME_VERIFIER_INPUT_PARAMETER));
        } catch (IllegalArgumentException e) {
            throw new ConnectorValidationException(
                    String.format(
                            "'%s' option is invalid for %s. Only one of %s is supported.",
                            hostNameVerifierParam,
                            HOSTNAME_VERIFIER_INPUT_PARAMETER,
                            Arrays.toString(SSLVerifier.values())));
        }
    }
}
