package com.aurapy.security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * Digital signature utilities for transaction signing and verification
 */
public class SignatureUtil {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 2048;

    /**
     * Generate RSA key pair
     * @return KeyPair containing public and private keys
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(KEY_SIZE);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }

    /**
     * Sign data with private key
     * @param data Data to sign
     * @param privateKey Private key for signing
     * @return Base64 encoded signature
     */
    public static String sign(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signedData = signature.sign();
            return Base64.getEncoder().encodeToString(signedData);
        } catch (Exception e) {
            throw new RuntimeException("Error signing data", e);
        }
    }

    /**
     * Verify signature with public key
     * @param data Original data
     * @param signature Signature to verify
     * @param publicKey Public key for verification
     * @return True if signature is valid
     */
    public static boolean verify(String data, String signature, PublicKey publicKey) {
        try {
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
            return sig.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Encode public key to string
     * @param publicKey Public key
     * @return Base64 encoded key
     */
    public static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * Encode private key to string
     * @param privateKey Private key
     * @return Base64 encoded key
     */
    public static String encodePrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * Decode public key from string
     * @param encodedKey Base64 encoded key
     * @return PublicKey
     */
    public static PublicKey decodePublicKey(String encodedKey) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding public key", e);
        }
    }

    /**
     * Decode private key from string
     * @param encodedKey Base64 encoded key
     * @return PrivateKey
     */
    public static PrivateKey decodePrivateKey(String encodedKey) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding private key", e);
        }
    }
}
