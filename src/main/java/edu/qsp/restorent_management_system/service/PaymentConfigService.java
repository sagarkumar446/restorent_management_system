package edu.qsp.restorent_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.model.PaymentConfig;
import edu.qsp.restorent_management_system.repository.PaymentConfigRepository;

@Service
public class PaymentConfigService {

    @Autowired
    private PaymentConfigRepository repo;

    private static final Integer CONFIG_ID = 1;

    /** Returns the current config, or an empty default if not yet saved. */
    public PaymentConfig getConfig() {
        return repo.findById(CONFIG_ID).orElseGet(() -> {
            PaymentConfig def = new PaymentConfig();
            def.setId(CONFIG_ID);
            return def;
        });
    }

    /** Saves / updates the config (upsert since id is always 1). */
    public PaymentConfig saveConfig(String keyId, String keySecret, boolean enabled) {
        PaymentConfig config = getConfig();
        config.setRazorpayKeyId(keyId.trim());
        config.setRazorpayKeySecret(keySecret.trim());
        config.setEnabled(enabled);
        return repo.save(config);
    }

    /** Returns masked config – key secret is partially hidden for security. */
    public PaymentConfig getMaskedConfig() {
        PaymentConfig cfg = getConfig();
        if (cfg.getRazorpayKeySecret() != null && cfg.getRazorpayKeySecret().length() > 6) {
            String masked = cfg.getRazorpayKeySecret().substring(0, 4)
                    + "••••••••••••"
                    + cfg.getRazorpayKeySecret().substring(cfg.getRazorpayKeySecret().length() - 4);
            cfg.setRazorpayKeySecret(masked);
        }
        return cfg;
    }
}
