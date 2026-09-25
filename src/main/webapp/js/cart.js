const ctxPath = document.querySelector('base')?.href || '/';

function showToast(message, isError = false) {
    const toast = document.getElementById('toast');
    if (!toast) return;
    toast.textContent = message;
    toast.className = 'toast show' + (isError ? ' toast-error' : '');
    setTimeout(() => { toast.className = 'toast'; }, 3000);
}

function updateCartBadge(count) {
    const badge = document.getElementById('cart-badge');
    if (!badge) return;
    if (count > 0) {
        badge.textContent = count;
        badge.classList.remove('hidden');
        badge.style.transform = 'scale(1.4)';
        setTimeout(() => { badge.style.transform = 'scale(1)'; }, 200);
    } else {
        badge.classList.add('hidden');
    }
}

async function addToCart(itemId, quantity = 1) {
    try {
        const res = await fetch(window.location.origin + '/api/cart/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ itemId, quantity })
        });
        const data = await res.json();

        if (!res.ok || !data.success) {
            showToast(data.error || 'Failed to add item', true);
            return;
        }
        updateCartBadge(data.cart.totalItems);
        showToast('✅ Added to cart');
    } catch (err) {
        showToast('Network error. Try again.', true);
        console.error(err);
    }
}

async function updateQuantity(cartItemId, quantity) {
    try {
        const res = await fetch(window.location.origin + '/api/cart/update', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ cartItemId, quantity })
        });
        const data = await res.json();
        if (!res.ok || !data.success) {
            showToast(data.error || 'Update failed', true);
            return;
        }
        // Reload page to reflect new totals
        location.reload();
    } catch (err) {
        showToast('Network error', true);
    }
}

async function removeItem(cartItemId) {
    try {
        const res = await fetch(window.location.origin + '/api/cart/remove', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ cartItemId })
        });
        const data = await res.json();
        if (!res.ok || !data.success) {
            showToast(data.error || 'Remove failed', true);
            return;
        }
        location.reload();
    } catch (err) {
        showToast('Network error', true);
    }
}

async function clearCart() {
    if (!confirm('Clear your entire cart?')) return;
    try {
        await fetch(window.location.origin + '/api/cart/clear', { method: 'POST' });
        updateCartBadge(0);
        location.reload();
    } catch (err) {
        showToast('Network error', true);
    }
}

// Wire up add-to-cart buttons
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
        btn.addEventListener('click', e => {
            e.preventDefault();
            addToCart(parseInt(btn.dataset.itemId), 1);
        });
    });
});