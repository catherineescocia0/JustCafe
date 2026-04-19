/* ============================================================
   secret-menu.js — Just Cafe Dedication Page
   ============================================================ */

// ---- Navbar scroll ----
const secretNav = document.getElementById('secretNav');
window.addEventListener('scroll', () => {
    secretNav.classList.toggle('scrolled', window.scrollY > 60);
});

// ---- Scroll reveal ----
const revealObs = new IntersectionObserver((entries) => {
    entries.forEach((entry, i) => {
        if (entry.isIntersecting) {
            setTimeout(() => entry.target.classList.add('visible'), i * 80);
            revealObs.unobserve(entry.target);
        }
    });
}, { threshold: 0.1, rootMargin: '0px 0px -60px 0px' });
document.querySelectorAll('.ded-reveal').forEach(el => revealObs.observe(el));

// ============================================================
// PART 1 — CIRCULAR ORBITAL GALLERY
// ============================================================

/**
 * PHOTO DATA — edit this object to add/replace/remove your photos.
 *
 * For each year, add entries to the `photos` array:
 *   src:     path to image, e.g. "/images/gallery/2019/opening-day.jpg"
 *   label:   short title shown in tooltip and center label
 *   sub:     subtitle / date shown below label
 *   caption: longer text shown in lightbox caption
 *
 * The orbital layout auto-sizes thumbnails:
 *   1–4  photos → large thumbs (110px)
 *   5–7  photos → medium thumbs (90px)
 *   8+   photos → small thumbs (72px)
 *
 * PLACEHOLDER MODE: while src is empty ("") or uses the
 * placeholder prefix, a coloured emoji circle is shown instead.
 * Replace src with your real image path to activate it.
 */
const GALLERY_DATA = {
    '2020': {
        emoji: '👥',
        photos: [
            { src: '/images/gallery/2020/snapchatfirstconvo.png', label: 'Snapchat first conversation', sub: '', caption: 'The first convesation we had', emoji: '' },
            { src: '/images/gallery/2020/confession.jpg', label: 'Our Confession', sub: '', caption: 'Our confession to each other.', emoji: '' },
            { src: '/images/gallery/2020/firstvideocall.jpg', label: 'The first time we laid eyes to each other', sub: '', caption: 'The first time we laid eyes to each other', emoji: '' }
        ]
    },
    '2021': {
        emoji: '🪟',
        photos: [
            { src: '/images/gallery/2021/2021_1.jpg', label: '', sub: '', caption: '', emoji: '🪟' },
            { src: '/images/gallery/2021/2021_2.jpg', label: '', sub: '', caption: '', emoji: '🌧️' },
            { src: '/images/gallery/2021/2021_3.jpg', label: '', sub: '', caption: '', emoji: '📦' },
        ]
    },
    '2022': {
        emoji: '🌱',
        photos: [
            { src: '/images/gallery/2022/2022_1.jpg', label: '', sub: '', caption: '', emoji: '🏙️' },
            { src: '/images/gallery/2022/2022_2.jpg', label: '', sub: '', caption: '', emoji: '🎓' },
            { src: '/images/gallery/2022/2022_3.jpg', label: '', sub: '', caption: '', emoji: '🌱' }
        ]
    },
    '2023': {
        emoji: '🔥',
        photos: [
            { src: '/images/gallery/2023/2023_1.jpg', label: '', sub: '', caption: '', emoji: '🔥' },
            { src: '/images/gallery/2023/2023_2.jpg', label: '', sub: '', caption: '', emoji: '🏪' },
            { src: '/images/gallery/2023/2023_3.jpg', label: '', sub: '', caption: '', emoji: '👨‍🍳' }
        ]
    },
    '2024': {
        emoji: '🫶',
        photos: [
            { src: '/images/gallery/2024/2024_1.jpg', label: '',  sub: '', caption: '',emoji: '🎨' },
            { src: '/images/gallery/2024/2024_2.jpg', label: '',  sub: '', caption: '',emoji: '🫶' },
            { src: '/images/gallery/2024/2024_3.jpg', label: '',  sub: '', caption: '',emoji: '☕' },
        ]
    },
    '2025': {
        emoji: '🌅',
        photos: [
            { src: '/images/gallery/2025/2025_1.jpg', label: '',  sub: '', caption: '', emoji: '🌅' },
            { src: '/images/gallery/2025/2025_2.jpg', label: '',  sub: '', caption: '', moji: '👨‍👩‍👧‍👦' },
            { src: '/images/gallery/2025/2025_3.jpg', label: '',  sub: '', caption: '', emoji: '🎂' }
        ]
    },
    '2026': {
        emoji: '🌅',
        photos: [
            { src: '/images/gallery/2026/2026_1.JPG', label: '',  sub: '',    caption: '', emoji: '🌅' },
            { src: '/images/gallery/2026/2026_2.JPG', label: '',  sub: '',    caption: '', emoji: '👨‍👩‍👧‍👦'},
            { src: '/images/gallery/2026/2026_3.jpg', label: '',  sub: '',    caption: '', emoji: '🎂' },
            { src: '/images/gallery/2026/2026_4.jpg', label: '',  sub: '',    caption: '', emoji: '🎂' }
        ]
    }
};

// Track active photo per year for lightbox
const activePhoto = {};

/**
 * Build orbital ring for a given year.
 * Positions photo thumbnails evenly around a circle.
 */
function buildRing(year) {
    const ring = document.getElementById('ring-' + year);
    if (!ring) return;
    const data   = GALLERY_DATA[year];
    if (!data) return;
    const photos = data.photos;
    const n      = photos.length;

    // Responsive radius — ring fills its container
    const containerSize = ring.offsetWidth || 480;
    const radius = containerSize * 0.38;

    // Thumb size based on count
    const thumbSize = n <= 4 ? 110 : n <= 6 ? 92 : 76;

    // Orbit guide circles
    ring.innerHTML = `
        <div class="orbit-guide" style="width:${radius*2}px;height:${radius*2}px"></div>
        <div class="orbit-guide" style="width:${radius*1.2}px;height:${radius*1.2}px;opacity:0.5"></div>
        <div class="orbit-center-dot"></div>
    `;

    photos.forEach((photo, i) => {
        // Distribute evenly, starting from top (-90°)
        const angleDeg = (360 / n) * i - 90;
        const angleRad = (angleDeg * Math.PI) / 180;
        const cx = containerSize / 2;
        const cy = containerSize / 2;
        const x  = cx + radius * Math.cos(angleRad);
        const y  = cy + radius * Math.sin(angleRad);

        const item = document.createElement('div');
        item.className = 'orbit-item';
        item.dataset.year  = year;
        item.dataset.index = i;
        item.style.left    = x + 'px';
        item.style.top     = y + 'px';
        item.style.width   = thumbSize + 'px';
        item.style.height  = thumbSize + 'px';

        const thumb = document.createElement('div');
        thumb.className = 'orbit-thumb';
        thumb.style.width  = thumbSize + 'px';
        thumb.style.height = thumbSize + 'px';

        if (photo.src) {
            const img = document.createElement('img');
            img.src = photo.src;
            img.alt = photo.label;
            img.loading = 'lazy';
            img.classList.add('loading');
            img.onload = () => img.classList.remove('loading');
            thumb.appendChild(img);
        } else {
            // Placeholder
            const ph = document.createElement('div');
            ph.className = 'orbit-thumb-placeholder';
            ph.innerHTML = `<span>${photo.emoji || '📷'}</span><span class="ph-label">${photo.label}</span>`;
            thumb.appendChild(ph);
        }

        const tooltip = document.createElement('div');
        tooltip.className = 'orbit-tooltip';
        tooltip.textContent = photo.label;

        item.appendChild(thumb);
        item.appendChild(tooltip);
        ring.appendChild(item);

        item.addEventListener('click', () => selectPhoto(year, i));
    });
}

/**
 * When a thumbnail is clicked:
 * - Update center display with the photo
 * - Mark thumbnail as active
 * - Update center label
 */
function selectPhoto(year, idx) {
    const data  = GALLERY_DATA[year];
    const photo = data.photos[idx];
    activePhoto[year] = idx;

    // Update active class on thumbs
    const ring = document.getElementById('ring-' + year);
    ring.querySelectorAll('.orbit-item').forEach((item, i) => {
        item.classList.toggle('active', i === idx);
    });

    // Update center image
    const centerWrap = document.getElementById('center-' + year);
    centerWrap.classList.add('has-image');

    // Remove old img if present
    let img = centerWrap.querySelector('img');
    if (!img) {
        img = document.createElement('img');
        img.addEventListener('click', () => openLightbox(year, idx));
        img.style.cursor = 'zoom-in';
        centerWrap.appendChild(img);
    }

    if (photo.src) {
        img.style.opacity = '0';
        img.src = photo.src;
        img.alt = photo.label;
        img.onload = () => { img.style.opacity = '1'; };
        // If already cached it may not fire onload
        if (img.complete) img.style.opacity = '1';
        img.style.display = 'block';
    } else {
        // No real image — show emoji in center large
        img.style.display = 'none';
        centerWrap.classList.remove('has-image');
        // Update placeholder text
        const ph = centerWrap.querySelector('.center-placeholder');
        if (ph) ph.querySelector('span:first-child').textContent = photo.emoji || '📷';
    }

    // Update label
    const label = document.getElementById('label-' + year);
    if (label) {
        label.innerHTML = `<strong>${photo.label}</strong>&ensp;<span style="opacity:0.5">${photo.sub}</span>`;
    }
}

// Build all rings on load; rebuild on resize
function buildAllRings() {
    Object.keys(GALLERY_DATA).forEach(year => buildRing(year));
}

window.addEventListener('DOMContentLoaded', buildAllRings);

let resizeTimer;
window.addEventListener('resize', () => {
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(buildAllRings, 200);
});

// ---- Year switcher ----
function selectYear(year, btn) {
    document.querySelectorAll('.year-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    document.querySelectorAll('.year-panel').forEach(p => p.classList.remove('active'));
    const panel = document.querySelector(`.year-panel[data-year="${year}"]`);
    if (panel) {
        panel.classList.add('active');
        buildRing(year); // rebuild in case container size changed
        // Auto-select first photo if not yet selected
        if (activePhoto[year] === undefined) {
            setTimeout(() => selectPhoto(year, 0), 60);
        }
        const stage = document.getElementById('galleryStage');
        if (stage) {
            const offset = stage.getBoundingClientRect().top + window.scrollY - 120;
            window.scrollTo({ top: offset, behavior: 'smooth' });
        }
    }
}

// Auto-select first photo of 2019 on load
window.addEventListener('DOMContentLoaded', () => {
    setTimeout(() => selectPhoto('2019', 0), 100);
});

// ============================================================
// LIGHTBOX
// ============================================================
let lightboxYear = null;
let lightboxIdx  = null;

function openLightbox(year, idx) {
    const photo = GALLERY_DATA[year].photos[idx];
    if (!photo || !photo.src) return;
    lightboxYear = year;
    lightboxIdx  = idx;
    document.getElementById('lightboxImg').src   = photo.src;
    document.getElementById('lightboxImg').alt   = photo.label;
    document.getElementById('lightboxLabel').textContent = photo.label;
    document.getElementById('lightboxSub').textContent   = photo.sub || '';
    document.getElementById('lightboxOverlay').classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeLightbox() {
    document.getElementById('lightboxOverlay').classList.remove('open');
    document.body.style.overflow = '';
}

function lightboxNav(dir, e) {
    if (e) e.stopPropagation();
    if (lightboxYear === null) return;
    const photos = GALLERY_DATA[lightboxYear].photos;
    lightboxIdx  = (lightboxIdx + dir + photos.length) % photos.length;
    const photo  = photos[lightboxIdx];
    if (!photo.src) { lightboxNav(dir, null); return; } // skip placeholders
    document.getElementById('lightboxImg').src   = photo.src;
    document.getElementById('lightboxLabel').textContent = photo.label;
    document.getElementById('lightboxSub').textContent   = photo.sub || '';
    selectPhoto(lightboxYear, lightboxIdx);
}

// Keyboard navigation
window.addEventListener('keydown', e => {
    const lb = document.getElementById('lightboxOverlay');
    if (!lb.classList.contains('open')) return;
    if (e.key === 'ArrowRight') lightboxNav(1, null);
    if (e.key === 'ArrowLeft')  lightboxNav(-1, null);
    if (e.key === 'Escape')     closeLightbox();
});

// ============================================================
// PART 2 — LETTERS CAROUSEL
// ============================================================
let currentLetterIdx = 0;
const CARD_GAP = 24;

function initLetterDots() {
    const track = document.getElementById('lettersTrack');
    const dots  = document.getElementById('lettersDots');
    if (!track || !dots) return;
    const cards = track.querySelectorAll('.letter-card');
    dots.innerHTML = '';
    cards.forEach((_, i) => {
        const dot = document.createElement('div');
        dot.className = 'letter-dot' + (i === 0 ? ' active' : '');
        dot.addEventListener('click', () => goToLetter(i));
        dots.appendChild(dot);
    });
}

function goToLetter(idx) {
    const track = document.getElementById('lettersTrack');
    if (!track) return;
    const cards = track.querySelectorAll('.letter-card');
    if (!cards.length) return;
    idx = Math.max(0, Math.min(idx, cards.length - 1));
    currentLetterIdx = idx;
    const cardW = cards[0].offsetWidth + CARD_GAP;
    track.scrollTo({ left: cardW * idx, behavior: 'smooth' });
    document.querySelectorAll('.letter-dot').forEach((d, i) => d.classList.toggle('active', i === idx));
}

function scrollLetters(dir) { goToLetter(currentLetterIdx + dir); }

document.addEventListener('DOMContentLoaded', () => {
    initLetterDots();
    const track = document.getElementById('lettersTrack');
    if (track) {
        track.addEventListener('scroll', () => {
            const cards = track.querySelectorAll('.letter-card');
            if (!cards.length) return;
            const cardW = cards[0].offsetWidth + CARD_GAP;
            const newIdx = Math.round(track.scrollLeft / cardW);
            if (newIdx !== currentLetterIdx) {
                currentLetterIdx = newIdx;
                document.querySelectorAll('.letter-dot').forEach((d, i) => d.classList.toggle('active', i === newIdx));
            }
        }, { passive: true });
    }
});

// ============================================================
// PART 3 — ENVELOPES & LETTER MODAL
// ============================================================
const LETTERS = {
    1: {
        wax: 'J',
        content: `<p>Do you ever wonder how life would be if we didn't meet ? We had that conversation long time ago, and I remember you said that you will probably got married,
        and still have the same goals as what you have now. I honestly believe that, if we haven't met at all, I bet that the person you are with is the luckiest,
        most blessed person will ever be, not only because it was you who is there partner but because I know how dedicated you are as partner. I have seen it,
        and you have been very true about it, even if I fight with you about your dedications, you still didn't break any of your promise. Loving you
        and being with you is the greatest event that had happen in my life, I have said it before too, your love for me is way too big for someone like me,
        I may look difficult but I think you have gotten to know me better than anybody else. With our relationship moving towards into something bigger,
        I prayed that we hold on to each other ever stronger, I believe that a lot of people are rooting for us,
         for our relationship, and some might be bitter about it, we don't mind them at all. At the end of the day, it is always us against the world.</p>`,
        sig: 'With all love and dedications, all for you!'
    },
    2: {
        wax: 'JxC',
        content: `<p>From the very start, we both know how our relationship is going to be. We thought and talked about the possible things that could happen, the fights that we might encounter, and the struggles that is along the way. We also
            talked about what we want, what we want to achieve; all throughout the years we've been together, we planned things together,
            we thought of how we will spend our lives together. Although, our relationship is not always rainbows and peace,
            we fought a lot, we fought too much to the point of breaking up, we reached each others limit, we gave up,
            and through those hardships we are now here, we're not perfect as what everyone might see or think, but we are still together, holding on. </p>`,
        sig: 'Us against the world.'
    }
};

function openEnvelope(num) {
    const letter = LETTERS[num];
    if (!letter) return;
    document.querySelectorAll('.envelope-wrap').forEach((w, i) => {
        if (i + 1 === num) w.classList.add('opened');
    });
    document.getElementById('letterModalWax').textContent    = letter.wax;
    document.getElementById('letterModalContent').innerHTML  = letter.content;
    document.getElementById('letterModalSig').textContent    = letter.sig;
    document.getElementById('letterModal').classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeLetter(e) {
    if (e && e.target !== document.getElementById('letterModal')) return;
    closeLetterDirect();
}
function closeLetterDirect() {
    document.getElementById('letterModal').classList.remove('open');
    document.body.style.overflow = '';
}

// ---- Toast ----
let toastTimer;
function smShowToast(msg) {
    const t = document.getElementById('smToast');
    t.textContent = msg;
    t.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => t.classList.remove('show'), 3000);
}
