package com.example.caporalescristos.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilidad JWT sin dependencias externas (solo Java estándar).
 * No usa io.jsonwebtoken ni Jackson.
 */
@Component
public class JwtUtil {

    private static final Base64.Encoder BASE64_URL = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();
    private static final Pattern SUB_PATTERN = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EXP_PATTERN = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");

    @Value("${app.jwt.secret:miClaveSecretaMuyLargaParaHS256AlMenos32Caracteres}")
    private String secret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private Long expirationMs;

    private byte[] getSigningKeyBytes() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, keyBytes.length);
            return padded;
        }
        return keyBytes;
    }

    public String generateToken(String username, String rol) {
        long now = System.currentTimeMillis() / 1000;
        long exp = now + (expirationMs / 1000);

        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"" + escapeJson(username) + "\",\"rol\":\"" + escapeJson(rol) + "\",\"iat\":" + now + ",\"exp\":" + exp + "}";

        try {
            String headerB64 = BASE64_URL.encodeToString(header.getBytes(StandardCharsets.UTF_8));
            String payloadB64 = BASE64_URL.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
            String signingInput = headerB64 + "." + payloadB64;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(getSigningKeyBytes(), "HmacSHA256"));
            byte[] signature = mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
            String sigB64 = BASE64_URL.encodeToString(signature);

            return signingInput + "." + sigB64;
        } catch (Exception e) {
            throw new RuntimeException("Error generando JWT", e);
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public String getUsernameFromToken(String token) {
        String payloadB64 = token.split("\\.")[1];
        String payload = new String(BASE64_URL_DECODER.decode(payloadB64), StandardCharsets.UTF_8);
        Matcher m = SUB_PATTERN.matcher(payload);
        return m.find() ? m.group(1) : null;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            String payloadB64 = token.split("\\.")[1];
            String payload = new String(BASE64_URL_DECODER.decode(payloadB64), StandardCharsets.UTF_8);
            Matcher m = EXP_PATTERN.matcher(payload);
            if (!m.find()) return true;
            long exp = Long.parseLong(m.group(1));
            return exp < (System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            return true;
        }
    }

    public Long getExpirationSeconds() {
        return expirationMs / 1000;
    }
}
