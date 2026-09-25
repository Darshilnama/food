package com.foodapp.swing;

import com.foodapp.swing.client.RestClient;
import com.foodapp.swing.client.SyncClient;

import javax.swing.*;
import java.awt.*;

public class MainDesktopApp extends JFrame {

    private final RestClient restClient;
    private final SyncClient syncClient;
    
    // For demonstration, assuming logged in as user 1
    private final Long MOCK_USER_ID = 1L; 

    public MainDesktopApp() {
        setTitle("FoodExpress - Desktop Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.restClient = new RestClient();
        this.syncClient = new SyncClient();

        initUI();
        startSync();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JButton loadRestaurantsBtn = new JButton("Load Restaurants");
        loadRestaurantsBtn.addActionListener(e -> {
            try {
                String json = restClient.getRestaurants();
                displayArea.setText("Restaurants Data:\n" + json);
            } catch (Exception ex) {
                displayArea.setText("Error fetching data: " + ex.getMessage());
            }
        });
        
        panel.add(loadRestaurantsBtn, BorderLayout.SOUTH);

        add(panel);
    }
    
    private void startSync() {
        // Connect to the WebSocket for real-time updates
        syncClient.connect(MOCK_USER_ID, eventPayload -> {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Received event: " + eventPayload);
                // Based on event type, we would refresh the UI here
                JOptionPane.showMessageDialog(this, "Real-time Event: " + eventPayload, "Sync Update", JOptionPane.INFORMATION_MESSAGE);
            });
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainDesktopApp().setVisible(true);
        });
    }
}
