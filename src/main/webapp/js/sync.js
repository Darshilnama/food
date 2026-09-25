if (window.userId) {
    const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // Get the base path dynamically from the document context if needed
    // Usually standard Java web apps map to a context path, let's assume /FoodDeliveryApp or whatever
    // We can inject the context path into window.contextPath in header.jsp, but wait, window.contextPath is better.
    const wsUrl = `${wsProtocol}//${window.location.host}${window.contextPath}/ws/events/${window.userId}`;
    const ws = new WebSocket(wsUrl);

    ws.onmessage = function(event) {
        console.log("Received realtime event:", event.data);
        
        // In a real application, we would check event.type and update the UI directly 
        // using DOM manipulation or a frontend framework. For this iteration, 
        // we'll simply reload the page to ensure the user gets the most authoritative state.
        // Or we could dispatch a CustomEvent and let specific pages decide to reload.
        
        // Dispatching event for potential listeners
        let parsed = null;
        try { parsed = JSON.parse(event.data); } catch (e) {}
        
        if (parsed && parsed.type) {
            document.dispatchEvent(new CustomEvent('appEvent', { detail: parsed }));
            
            // For now, if we receive an ORDER_UPDATED or PAYMENT_UPDATED, we refresh if we are on orders/bill pages
            if (parsed.type === 'ORDER_UPDATED' || parsed.type === 'PAYMENT_UPDATED') {
                if (window.location.pathname.includes('/orders') || window.location.pathname.includes('/bill')) {
                    window.location.reload();
                }
            }
        }
    };

    ws.onclose = function() {
        console.log("Real-time connection closed.");
    };
    
    ws.onerror = function(error) {
        console.error("WebSocket error:", error);
    };
}
