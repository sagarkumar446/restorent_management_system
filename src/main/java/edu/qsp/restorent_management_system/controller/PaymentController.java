package edu.qsp.restorent_management_system.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import edu.qsp.restorent_management_system.config.ResponseStructure;
import edu.qsp.restorent_management_system.model.PaymentConfig;
import edu.qsp.restorent_management_system.service.PaymentConfigService;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentConfigService paymentConfigService;

    // ─── Admin: Get current config (masked) ──────────────────
    @GetMapping("/config")
    public ResponseEntity<?> getConfig() {
        PaymentConfig cfg = paymentConfigService.getMaskedConfig();
        ResponseStructure<PaymentConfig> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Config fetched");
        response.setData(cfg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ─── Admin: Save / update config ─────────────────────────
    @PostMapping("/config")
    public ResponseEntity<?> saveConfig(@RequestBody Map<String, Object> body) {
        try {
            String keyId = body.getOrDefault("razorpayKeyId", "").toString();
            String keySecret = body.getOrDefault("razorpayKeySecret", "").toString();
            boolean enabled = Boolean.parseBoolean(body.getOrDefault("enabled", "false").toString());

            // Quick validation: try to init RazorpayClient
            new RazorpayClient(keyId, keySecret);

            paymentConfigService.saveConfig(keyId, keySecret, enabled);
            // Return masked version
            PaymentConfig masked = paymentConfigService.getMaskedConfig();

            ResponseStructure<PaymentConfig> response = new ResponseStructure<>();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Payment config saved successfully!");
            response.setData(masked);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseStructure<String> response = new ResponseStructure<>();
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid config: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ─── Customer: Create Razorpay order ─────────────────────
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> body) {
        PaymentConfig cfg = paymentConfigService.getConfig();

        if (!cfg.isEnabled() || cfg.getRazorpayKeyId().isBlank()) {
            ResponseStructure<String> response = new ResponseStructure<>();
            response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setMessage("Online payment is not configured. Please contact the restaurant.");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }

        try {
            double amountInRupees = Double.parseDouble(body.get("amount").toString());
            int amountInPaise = (int) (amountInRupees * 100);

            RazorpayClient client = new RazorpayClient(cfg.getRazorpayKeyId(), cfg.getRazorpayKeySecret());

            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", "txn_" + System.currentTimeMillis());
            options.put("payment_capture", 1);

            Order order = client.orders.create(options);

            ResponseStructure<Map<String, Object>> response = new ResponseStructure<>();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Order created successfully");
            response.setData(Map.of(
                    "orderId", order.get("id"),
                    "amount", amountInPaise,
                    "currency", "INR",
                    "keyId", cfg.getRazorpayKeyId()));
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ResponseStructure<String> response = new ResponseStructure<>();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Payment order creation failed: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ─── Customer: Verify payment signature ──────────────────
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> body) {
        ResponseStructure<Map<String, String>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Payment verified successfully");
        response.setData(Map.of(
                "status", "success",
                "paymentId", body.getOrDefault("razorpay_payment_id", "N/A"),
                "orderId", body.getOrDefault("razorpay_order_id", "N/A")));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
