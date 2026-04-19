/* ============================================================
   main.js — Just Cafe Frontend Logic
   ============================================================ */

// ---- Navbar scroll state ----
const navbar = document.getElementById('navbar');
window.addEventListener('scroll', () => {
    navbar.classList.toggle('scrolled', window.scrollY > 50);
});

// ---- Mobile menu ----
const hamburger = document.getElementById('hamburger');
const mobileMenu = document.getElementById('mobileMenu');
hamburger.addEventListener('click', () => {
    mobileMenu.classList.toggle('open');
});
function closeMobileMenu() {
    mobileMenu.classList.remove('open');
}

// ---- Scroll reveal ----
const revealObserver = new IntersectionObserver((entries) => {
    entries.forEach((entry, i) => {
        if (entry.isIntersecting) {
            setTimeout(() => entry.target.classList.add('visible'), i * 80);
            revealObserver.unobserve(entry.target);
        }
    });
}, { threshold: 0.12, rootMargin: '0px 0px -60px 0px' });

document.querySelectorAll('.reveal').forEach(el => revealObserver.observe(el));

// ---- Menu tabs ----
document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.menu-panel').forEach(p => p.classList.remove('active'));
        btn.classList.add('active');
        const tab = btn.dataset.tab;
        const panel = document.getElementById('tab-' + tab);
        if (panel) panel.classList.add('active');
    });
});

// ---- Sub-filter tabs ----
document.querySelectorAll('.menu-sub-tabs').forEach(subTabGroup => {
    subTabGroup.addEventListener('click', (e) => {
        const btn = e.target.closest('.sub-tab');
        if (!btn) return;
        subTabGroup.querySelectorAll('.sub-tab').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        const filter = btn.dataset.filter;
        const panel = subTabGroup.closest('.menu-panel');
        panel.querySelectorAll('.menu-card').forEach(card => {
            if (filter === 'ALL' || card.dataset.sub === filter) {
                card.classList.remove('hidden');
            } else {
                card.classList.add('hidden');
            }
        });
    });
});

// ---- Customization builder data (from Thymeleaf) ----
function groupBy(arr, key) {
    return arr.reduce((acc, item) => {
        const g = item[key];
        if (!acc[g]) acc[g] = [];
        acc[g].push(item);
        return acc;
    }, {});
}

// Groups that allow picking multiple options simultaneously
const MULTI_SELECT_GROUPS = new Set(['Syrup', 'Extras', 'Add-ons', 'Dietary']);

function buildCustomChips(data, containerId, type) {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = '';

    const groups = groupBy(data, 'groupName');
    Object.entries(groups).forEach(([groupName, options]) => {
        const block = document.createElement('div');
        block.className = 'custom-group-block';

        const isMulti = MULTI_SELECT_GROUPS.has(groupName);
        const label = document.createElement('div');
        label.className = 'custom-group-label';
        label.innerHTML = groupName + (isMulti
            ? ' <span class="group-type-tag">multi-select</span>'
            : ' <span class="group-type-tag single">pick one</span>');
        block.appendChild(label);

        const optDiv = document.createElement('div');
        optDiv.className = 'custom-options';
        optDiv.dataset.multiselect = isMulti ? 'true' : 'false';

        options.forEach(opt => {
            const chip = document.createElement('button');
            chip.className = 'custom-chip';
            chip.dataset.price = opt.additionalPrice || 0;
            chip.dataset.group = groupName;
            chip.dataset.name = opt.optionName;
            chip.type = 'button';
            const priceLabel = opt.additionalPrice > 0
                ? ` <span class="chip-price">+₱${opt.additionalPrice.toFixed(2)}</span>`
                : '';
            chip.innerHTML = opt.optionName + priceLabel;
            chip.addEventListener('click', () => toggleChip(chip, type, isMulti));
            optDiv.appendChild(chip);
        });

        block.appendChild(optDiv);
        container.appendChild(block);
    });
}

function toggleChip(chip, type, isMulti) {
    const group = chip.dataset.group;
    const container = chip.closest('#drinkCustomContainer, #foodCustomContainer');
    const isCurrentlySelected = chip.classList.contains('selected');

    if (isMulti) {
        // Multi-select: just toggle this chip independently
        chip.classList.toggle('selected');
    } else {
        // Single-select: clicking a selected chip deselects it (unselect); clicking another selects it
        container.querySelectorAll(`.custom-chip[data-group="${group}"]`).forEach(c => c.classList.remove('selected'));
        if (!isCurrentlySelected) {
            chip.classList.add('selected');
        }
        // If it WAS selected, all are now cleared = deselected state
    }

    type === 'drink' ? updateDrinkPrice() : updateFoodPrice();
}

// Initialize customization builders
buildCustomChips(drinkCustomizationsData, 'drinkCustomContainer', 'drink');
buildCustomChips(foodCustomizationsData, 'foodCustomContainer', 'food');

// ---- Price calculations ----
function getBasePrice(selectId) {
    const sel = document.getElementById(selectId);
    const opt = sel.options[sel.selectedIndex];
    return opt ? (parseFloat(opt.dataset.price) || 0) : 0;
}

function getSelectedAddons(containerId) {
    const container = document.getElementById(containerId);
    let total = 0;
    const selections = [];
    container.querySelectorAll('.custom-chip.selected').forEach(chip => {
        total += parseFloat(chip.dataset.price) || 0;
        selections.push({ group: chip.dataset.group, name: chip.dataset.name, price: parseFloat(chip.dataset.price) || 0 });
    });
    return { total, selections };
}

function updateDrinkPrice() {
    const base = getBasePrice('drinkSelect');
    const { total } = getSelectedAddons('drinkCustomContainer');
    const grand = base + total;
    document.getElementById('drinkTotal').textContent = '₱' + grand.toFixed(2);
}

function updateFoodPrice() {
    const base = getBasePrice('foodSelect');
    const { total } = getSelectedAddons('foodCustomContainer');
    const grand = base + total;
    document.getElementById('foodTotal').textContent = '₱' + grand.toFixed(2);
}

// ---- Toggle builder between drink/food ----
let currentBuilder = 'drink';
function toggleBuilder(type) {
    currentBuilder = type;
    const drinkBuilder = document.getElementById('drinkBuilder');
    const foodBuilder = document.getElementById('foodBuilder');
    const toggleBtn = document.querySelector('.builder-toggle');
    const header = document.querySelector('.builder-header h3');

    if (type === 'food') {
        drinkBuilder.classList.remove('active');
        foodBuilder.classList.add('active');
        toggleBtn.textContent = 'Switch to Drink ›';
        header.textContent = 'Build Your Meal';
        toggleBtn.onclick = () => toggleBuilder('drink');
    } else {
        foodBuilder.classList.remove('active');
        drinkBuilder.classList.add('active');
        toggleBtn.textContent = 'Switch to Food ›';
        header.textContent = 'Build Your Drink';
        toggleBtn.onclick = () => toggleBuilder('food');
    }
}

// ---- Order cart ----
let cart = [];

function addCustomOrder(type) {
    const isDrink = type === 'drink';
    const sel = document.getElementById(isDrink ? 'drinkSelect' : 'foodSelect');
    const notes = document.getElementById(isDrink ? 'drinkNotes' : 'foodNotes').value;
    const selectedOpt = sel.options[sel.selectedIndex];

    if (!sel.value) {
        showToast('Please select an item first!');
        return;
    }

    const basePrice = parseFloat(selectedOpt.dataset.price) || 0;
    const { total: addonTotal, selections } = getSelectedAddons(isDrink ? 'drinkCustomContainer' : 'foodCustomContainer');
    const totalPrice = basePrice + addonTotal;

    const customSummary = selections.map(s => `${s.group}: ${s.name}`).join(', ');
    const itemId = parseInt(sel.value);

    cart.push({
        id: Date.now(),
        itemId,
        name: selectedOpt.text.split(' — ')[0],
        basePrice,
        customizations: customSummary,
        specialInstructions: notes,
        totalPrice,
        category: isDrink ? 'DRINK' : 'FOOD'
    });

    renderCart();
    showToast(`✓ ${sel.options[sel.selectedIndex].text.split(' — ')[0]} added to order!`);

    // Reset
    sel.value = '';
    document.getElementById(isDrink ? 'drinkCustomContainer' : 'foodCustomContainer')
        .querySelectorAll('.custom-chip.selected')
        .forEach(c => c.classList.remove('selected'));
    document.getElementById(isDrink ? 'drinkNotes' : 'foodNotes').value = '';
    isDrink ? updateDrinkPrice() : updateFoodPrice();
}

function quickAdd(btn) {
    const card = btn.closest('.menu-card');
    const name = card.dataset.name;
    const price = parseFloat(card.dataset.price);
    const category = card.dataset.category;
    const itemId = parseInt(card.dataset.id);

    cart.push({
        id: Date.now(),
        itemId,
        name,
        basePrice: price,
        customizations: '',
        specialInstructions: '',
        totalPrice: price,
        category
    });

    renderCart();
    showToast(`✓ ${name} added!`);
}

function removeFromCart(id) {
    cart = cart.filter(item => item.id !== id);
    renderCart();
}

function clearOrder() {
    cart = [];
    renderCart();
}

function renderCart() {
    const container = document.getElementById('orderItems');
    const footer = document.getElementById('orderFooter');
    const countBadge = document.getElementById('orderCount');
    const grandTotal = document.getElementById('grandTotal');

    countBadge.textContent = cart.length;

    if (cart.length === 0) {
        container.innerHTML = '<div class="order-empty">Your order is empty.<br>Add something delicious!</div>';
        footer.style.display = 'none';
        return;
    }

    let total = 0;
    container.innerHTML = cart.map(item => {
        total += item.totalPrice;
        return `
            <div class="order-item">
                <span class="order-item-remove" onclick="removeFromCart(${item.id})">✕</span>
                <div class="order-item-name">${item.name}</div>
                ${item.customizations ? `<div class="order-item-custom">${item.customizations}</div>` : ''}
                ${item.specialInstructions ? `<div class="order-item-custom">Note: ${item.specialInstructions}</div>` : ''}
                <div class="order-item-price">₱${item.totalPrice.toFixed(2)}</div>
            </div>
        `;
    }).join('');

    grandTotal.textContent = '₱' + total.toFixed(2);
    footer.style.display = 'block';
}

// ---- Order modal ----
function openOrderModal() {
    if (cart.length === 0) {
        showToast('Add items to your order first!');
        return;
    }
    const modal = document.getElementById('orderModal');
    const list = document.getElementById('modalOrderList');
    const modalTotal = document.getElementById('modalGrandTotal');

    let total = 0;
    list.innerHTML = cart.map(item => {
        total += item.totalPrice;
        return `
            <div class="modal-order-item">
                <span>${item.name}${item.customizations ? ' (' + item.customizations.substring(0, 40) + (item.customizations.length > 40 ? '...' : '') + ')' : ''}</span>
                <span>₱${item.totalPrice.toFixed(2)}</span>
            </div>
        `;
    }).join('');
    modalTotal.textContent = '₱' + total.toFixed(2);
    modal.classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeOrderModal() {
    document.getElementById('orderModal').classList.remove('open');
    document.body.style.overflow = '';
}

// Close modal on backdrop click
document.getElementById('orderModal').addEventListener('click', (e) => {
    if (e.target === e.currentTarget) closeOrderModal();
});

async function submitOrder() {
    const customerName = document.getElementById('customerName').value.trim();
    if (!customerName) {
        showToast('Please enter your name!');
        return;
    }

    const submitBtn = document.querySelector('#orderModal .btn-primary');
    submitBtn.textContent = 'Placing Order...';
    submitBtn.disabled = true;

    const orders = cart.map(item => ({
        customerName,
        itemName: item.name,
        quantity: 1,
        totalPrice: item.totalPrice,
        customizations: item.customizations,
        specialInstructions: item.specialInstructions
    }));

    try {
        const results = await Promise.all(orders.map(order =>
            fetch('/api/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(order)
            }).then(r => r.json())
        ));

        closeOrderModal();
        cart = [];
        renderCart();

        const firstId = results[0]?.orderId;
        showToast(`🎉 Order confirmed! #${firstId} — We'll have it ready for you!`);
        document.getElementById('customerName').value = '';
    } catch (err) {
        showToast('Something went wrong. Please try again.');
    } finally {
        submitBtn.textContent = 'Place Order';
        submitBtn.disabled = false;
    }
}

// ---- Toast notification ----
let toastTimer;
function showToast(msg) {
    const toast = document.getElementById('toast');
    toast.textContent = msg;
    toast.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove('show'), 3500);
}

// ---- Smooth scroll for nav links ----
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', (e) => {
        const href = anchor.getAttribute('href');
        if (href === '#') return;
        e.preventDefault();
        const target = document.querySelector(href);
        if (target) {
            const offset = target.getBoundingClientRect().top + window.scrollY - 72;
            window.scrollTo({ top: offset, behavior: 'smooth' });
        }
    });
});

// ---- Secret code modal ----
function openSecretModal() {
    document.getElementById('secretModal').classList.add('open');
    document.getElementById('secretCodeInput').value = '';
    document.getElementById('secretError').textContent = '';
    setTimeout(() => document.getElementById('secretCodeInput').focus(), 100);
}

function closeSecretModal() {
    document.getElementById('secretModal').classList.remove('open');
    document.getElementById("secretModal").style.display = "none";
}

document.addEventListener('DOMContentLoaded', () => {
    const overlay = document.getElementById('secretModal');
    if (overlay) {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) closeSecretModal();
        });
    }
});

async function submitSecretCode() {
    const input = document.getElementById('secretCodeInput');
    const errorEl = document.getElementById('secretError');
    const code = input.value.trim();
    const btn = document.querySelector('.secret-submit-btn');

    if (!code) { errorEl.textContent = 'Please enter a code.'; return; }

    btn.textContent = '...';
    btn.disabled = true;
    errorEl.textContent = '';

    try {
        const res = await fetch('/api/secret/validate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ code })
        });
        const data = await res.json();
        if (data.valid) {
            btn.textContent = '✓';
            input.style.borderColor = '#5a8a5a';
            setTimeout(() => { window.location.href = data.redirect; }, 500);
        } else {
            errorEl.textContent = data.message || 'Invalid code.';
            input.style.borderColor = '#8a2020';
            input.value = '';
            btn.textContent = 'Enter';
            btn.disabled = false;
            input.focus();
        }
    } catch (e) {
        errorEl.textContent = 'Something went wrong.';
        btn.textContent = 'Enter';
        btn.disabled = false;
    }
}
