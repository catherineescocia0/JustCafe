/* ============================================================
   admin.js — Just Cafe Admin Dashboard Logic
   ============================================================ */

// ---- Live clock ----
function updateClock() {
    const el = document.getElementById('sidebarTime');
    if (el) el.textContent = new Date().toLocaleTimeString('en-PH', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}
updateClock();
setInterval(updateClock, 1000);

// ---- Sidebar toggle (mobile) ----
function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('open');
}

// ---- Panel navigation ----
const PANEL_TITLES = { dashboard: 'Dashboard', orders: 'Orders', inventory: 'Inventory' };

function showPanel(name, clickedEl) {
    document.querySelectorAll('.panel').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));

    const panel = document.getElementById('panel-' + name);
    if (panel) panel.classList.add('active');

    if (clickedEl) clickedEl.classList.add('active');
    else {
        const navEl = document.querySelector(`.nav-item[data-panel="${name}"]`);
        if (navEl) navEl.classList.add('active');
    }

    document.getElementById('topbarTitle').textContent = PANEL_TITLES[name] || name;
    // Close mobile sidebar
    document.getElementById('sidebar').classList.remove('open');
    return false;
}

// ---- Order status filter ----
function filterOrders(status, btn) {
    document.querySelectorAll('.order-tab').forEach(b => b.classList.remove('active'));
    if (btn) btn.classList.add('active');

    const cards = document.querySelectorAll('#ordersGrid .order-card');
    let visible = 0;
    cards.forEach(card => {
        const show = (status === 'ALL') || (card.dataset.status === status);
        card.style.display = show ? '' : 'none';
        if (show) visible++;
    });
    const emptyEl = document.getElementById('ordersEmpty');
    if (emptyEl) emptyEl.style.display = visible === 0 ? 'block' : 'none';
}

// ---- Update order status via API ----
async function updateStatus(orderId, newStatus) {
    try {
        const res = await fetch(`/api/admin/orders/${orderId}/status`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });
        const data = await res.json();
        if (data.success) {
            showAdminToast(`✓ Order #${orderId} → ${newStatus}`);
            setTimeout(() => location.reload(), 700);
        } else {
            showAdminToast('Error: ' + data.message);
        }
    } catch (e) {
        showAdminToast('Network error. Please try again.');
    }
}

// ---- Delete order via API ----
async function deleteOrder(orderId) {
    if (!confirm(`Delete order #${orderId}? This cannot be undone.`)) return;
    try {
        await fetch(`/api/admin/orders/${orderId}`, { method: 'DELETE' });
        const card = document.querySelector(`.order-card[data-id="${orderId}"]`);
        if (card) {
            card.style.transition = 'opacity 0.3s, transform 0.3s';
            card.style.opacity = '0';
            card.style.transform = 'scale(0.95)';
            setTimeout(() => { card.remove(); refreshSummary(); }, 300);
        }
        showAdminToast(`🗑 Order #${orderId} dismissed`);
    } catch (e) {
        showAdminToast('Error deleting order.');
    }
}

// ---- Inventory category filter ----
function filterInventory(cat, btn) {
    document.querySelectorAll('.inv-tab').forEach(b => b.classList.remove('active'));
    if (btn) btn.classList.add('active');

    const rows = document.querySelectorAll('#invTableBody tr');
    rows.forEach(row => {
        if (cat === 'ALL') {
            row.classList.remove('hidden-row');
        } else if (cat === 'LOW') {
            const status = row.dataset.status;
            row.classList.toggle('hidden-row', status !== 'LOW' && status !== 'OUT_OF_STOCK');
        } else {
            row.classList.toggle('hidden-row', row.dataset.cat !== cat);
        }
    });
}

// ---- Inventory search ----
function searchInventory(query) {
    const q = query.toLowerCase().trim();
    const rows = document.querySelectorAll('#invTableBody tr');
    rows.forEach(row => {
        const name = (row.dataset.name || '').toLowerCase();
        row.classList.toggle('hidden-row', q.length > 0 && !name.includes(q));
    });
}

// ---- Quick stock adjust (+/- buttons in table) ----
async function adjustStock(itemId, delta) {
    try {
        const res = await fetch(`/api/admin/inventory/${itemId}/adjust`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ delta })
        });
        const data = await res.json();
        if (data.success) {
            // Update the stock value in place
            const valEl = document.getElementById('stock-' + itemId);
            if (valEl) valEl.textContent = data.newQuantity;

            // Update status pill
            const statusEl = document.getElementById('status-' + itemId);
            if (statusEl) {
                statusEl.className = 'status-pill';
                if (data.status === 'OUT_OF_STOCK') {
                    statusEl.classList.add('out-of-stock');
                    statusEl.textContent = 'Out of Stock';
                } else if (data.status === 'LOW') {
                    statusEl.classList.add('low-stock');
                    statusEl.textContent = 'Low Stock';
                } else {
                    statusEl.classList.add('in-stock');
                    statusEl.textContent = 'In Stock';
                }
            }

            // Update row highlight
            const row = valEl ? valEl.closest('tr') : null;
            if (row) {
                row.classList.remove('row-low', 'row-out');
                if (data.status === 'OUT_OF_STOCK') row.classList.add('row-out');
                else if (data.status === 'LOW') row.classList.add('row-low');
            }

            showAdminToast(`${data.itemName}: ${data.newQuantity} remaining`);
        }
    } catch (e) {
        showAdminToast('Error updating stock.');
    }
}

// ---- Restock modal ----
let activeRestockId = null;

function openRestockModal(itemId, itemName, currentQty) {
    activeRestockId = itemId;
    document.getElementById('restockItemName').textContent = itemName;
    document.getElementById('restockCurrent').textContent = currentQty;
    document.getElementById('restockQty').value = currentQty;
    document.getElementById('restockModal').classList.add('open');
}

function closeRestockModal() {
    document.getElementById('restockModal').classList.remove('open');
    activeRestockId = null;
}

function adjustRestockVal(delta) {
    const input = document.getElementById('restockQty');
    input.value = Math.max(0, (parseInt(input.value) || 0) + delta);
}

async function submitRestock() {
    if (!activeRestockId) return;
    const qty = parseInt(document.getElementById('restockQty').value);
    if (isNaN(qty) || qty < 0) {
        showAdminToast('Please enter a valid quantity.');
        return;
    }
    try {
        const res = await fetch(`/api/admin/inventory/${activeRestockId}/stock`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ quantity: qty })
        });
        const data = await res.json();
        if (data.success) {
            // Update table cell
            const valEl = document.getElementById('stock-' + activeRestockId);
            if (valEl) valEl.textContent = data.newQuantity;
            const statusEl = document.getElementById('status-' + activeRestockId);
            if (statusEl) {
                statusEl.className = 'status-pill';
                if (data.status === 'OUT_OF_STOCK') { statusEl.classList.add('out-of-stock'); statusEl.textContent = 'Out of Stock'; }
                else if (data.status === 'LOW') { statusEl.classList.add('low-stock'); statusEl.textContent = 'Low Stock'; }
                else { statusEl.classList.add('in-stock'); statusEl.textContent = 'In Stock'; }
            }
            const row = valEl ? valEl.closest('tr') : null;
            if (row) {
                row.classList.remove('row-low', 'row-out');
                if (data.status === 'OUT_OF_STOCK') row.classList.add('row-out');
                else if (data.status === 'LOW') row.classList.add('row-low');
            }
            showAdminToast(`✓ ${data.itemName} restocked to ${data.newQuantity}`);
            closeRestockModal();
        }
    } catch (e) {
        showAdminToast('Error updating stock.');
    }
}

// ---- Close modal on backdrop ----
document.getElementById('restockModal').addEventListener('click', e => {
    if (e.target === e.currentTarget) closeRestockModal();
});

// ---- Refresh summary stats from API ----
async function refreshSummary() {
    try {
        const res = await fetch('/api/admin/summary');
        const data = await res.json();
        const set = (id, val) => { const el = document.getElementById(id); if (el) el.textContent = val; };
        set('dc-pending', data.pendingCount);
        set('dc-confirmed', data.confirmedCount);
        set('dc-ready', data.readyCount);
        set('dc-total', data.totalOrders);
        set('dc-lowstock', data.lowStockCount);
        set('dc-outofstock', data.outOfStockCount);
        set('sidebarPendingCount', data.pendingCount);
        const lowBadge = document.getElementById('sidebarLowCount');
        if (lowBadge) {
            lowBadge.textContent = data.lowStockCount > 0 ? data.lowStockCount + ' low' : '';
        }
    } catch (e) { /* silent fail */ }
}

// ---- Full refresh (reload page) ----
function refreshAll() {
    showAdminToast('↻ Refreshing...');
    setTimeout(() => location.reload(), 400);
}

// ---- Auto-refresh every 30 seconds ----
setInterval(refreshSummary, 30000);

// ---- Toast notification ----
let toastTimer;
function showAdminToast(msg) {
    const toast = document.getElementById('adminToast');
    toast.textContent = msg;
    toast.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove('show'), 3200);
}