/**
 * coffee-canvas.js
 * Renders a coffee-being-poured animation tied to scroll position.
 * Uses HTML5 Canvas to draw an arc of falling coffee liquid and splashes.
 */

(function () {
    const canvas = document.getElementById('coffeeCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    // Resize canvas to fill window
    function resize() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    }
    resize();
    window.addEventListener('resize', resize);

    // ---- Coffee drop particles ----
    const drops = [];
    const MAX_DROPS = 80;

    function createDrop(progress) {
        // Spout position (top-center area, like a kettle pouring)
        const spoutX = canvas.width * 0.72 + Math.random() * 60 - 30;
        const spoutY = canvas.height * 0.05;

        drops.push({
            x: spoutX,
            y: spoutY,
            vx: (Math.random() - 0.6) * 2.5,
            vy: Math.random() * 1 + 2,
            r: Math.random() * 3 + 1.5,
            alpha: Math.random() * 0.6 + 0.4,
            life: 1.0,
            decay: Math.random() * 0.008 + 0.004,
            type: 'drop'
        });
    }

    // ---- Stream segments ----
    const streamPoints = [];

    function buildStream(progress) {
        streamPoints.length = 0;
        const streamLength = Math.floor(progress * 40);
        let x = canvas.width * 0.73;
        let y = canvas.height * 0.06;
        let vx = -0.8;
        let vy = 0;

        for (let i = 0; i < streamLength; i++) {
            streamPoints.push({ x, y, width: Math.max(0.5, 4 - i * 0.08) });
            vy += 0.5; // gravity
            vx += 0.04;
            x += vx;
            y += vy;
            if (y > canvas.height) break;
        }
    }

    // ---- Ripple / splash on "landing" ----
    const ripples = [];

    function addRipple(x, y) {
        ripples.push({ x, y, r: 0, maxR: Math.random() * 40 + 20, alpha: 0.5, speed: Math.random() * 1.5 + 1 });
    }

    // ---- Colour palette ----
    const COFFEE_COLOR = 'rgba(80, 40, 10,';
    const CREAM_COLOR = 'rgba(220, 170, 100,';
    const FOAM_COLOR = 'rgba(240, 200, 140,';

    // ---- Main draw ----
    let scrollProgress = 0;
    let targetProgress = 0;
    let frameCount = 0;

    function updateScrollProgress() {
        const maxScroll = document.documentElement.scrollHeight - window.innerHeight;
        targetProgress = maxScroll > 0 ? window.scrollY / maxScroll : 0;
    }

    window.addEventListener('scroll', () => {
        updateScrollProgress();
        // Spawn drops as user scrolls
        if (Math.random() < 0.5 + scrollProgress) {
            createDrop(scrollProgress);
        }
        // Occasionally add ripples near bottom of stream
        if (streamPoints.length > 0 && Math.random() < 0.08) {
            const last = streamPoints[streamPoints.length - 1];
            addRipple(last.x, last.y);
        }
    }, { passive: true });

    function draw() {
        // Smooth scroll progress
        scrollProgress += (targetProgress - scrollProgress) * 0.06;

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        if (scrollProgress < 0.01) {
            requestAnimationFrame(draw);
            return;
        }

        // ---- Draw stream ----
        buildStream(scrollProgress);
        if (streamPoints.length > 1) {
            ctx.beginPath();
            ctx.moveTo(streamPoints[0].x, streamPoints[0].y);
            for (let i = 1; i < streamPoints.length; i++) {
                ctx.lineTo(streamPoints[i].x, streamPoints[i].y);
            }
            // Gradient along stream
            const grad = ctx.createLinearGradient(
                streamPoints[0].x, streamPoints[0].y,
                streamPoints[streamPoints.length - 1].x, streamPoints[streamPoints.length - 1].y
            );
            grad.addColorStop(0, `rgba(60, 30, 5, ${scrollProgress * 0.9})`);
            grad.addColorStop(0.5, `rgba(90, 50, 15, ${scrollProgress * 0.75})`);
            grad.addColorStop(1, `rgba(110, 65, 20, ${scrollProgress * 0.5})`);
            ctx.strokeStyle = grad;
            ctx.lineWidth = 4 * scrollProgress + 1;
            ctx.lineCap = 'round';
            ctx.stroke();

            // Add secondary thinner highlight
            ctx.beginPath();
            ctx.moveTo(streamPoints[0].x - 1, streamPoints[0].y);
            for (let i = 1; i < streamPoints.length; i++) {
                ctx.lineTo(streamPoints[i].x - 1, streamPoints[i].y);
            }
            ctx.strokeStyle = `rgba(180, 120, 60, ${scrollProgress * 0.25})`;
            ctx.lineWidth = 1.5;
            ctx.stroke();
        }

        // ---- Draw kettle spout hint ----
        if (scrollProgress > 0.02) {
            const sx = canvas.width * 0.73;
            const sy = canvas.height * 0.04;
            ctx.beginPath();
            ctx.ellipse(sx, sy, 22 * scrollProgress + 8, 14 * scrollProgress + 5, -0.2, 0, Math.PI * 2);
            ctx.fillStyle = `rgba(50, 25, 5, ${scrollProgress * 0.7})`;
            ctx.fill();

            // Spout highlight
            ctx.beginPath();
            ctx.ellipse(sx - 4, sy - 3, 8, 5, -0.2, 0, Math.PI * 2);
            ctx.fillStyle = `rgba(130, 80, 30, ${scrollProgress * 0.4})`;
            ctx.fill();
        }

        // ---- Update & draw drops ----
        frameCount++;
        for (let i = drops.length - 1; i >= 0; i--) {
            const d = drops[i];
            d.x += d.vx;
            d.y += d.vy;
            d.vy += 0.15; // gravity
            d.life -= d.decay;

            if (d.life <= 0 || d.y > canvas.height + 20) {
                drops.splice(i, 1);
                continue;
            }

            ctx.beginPath();
            ctx.arc(d.x, d.y, d.r * d.life, 0, Math.PI * 2);

            // Drops further down become lighter (cream mixing in)
            const depthRatio = Math.min(d.y / canvas.height, 1);
            const r = Math.floor(80 + depthRatio * 60);
            const g = Math.floor(40 + depthRatio * 40);
            const b = Math.floor(10 + depthRatio * 20);
            ctx.fillStyle = `rgba(${r}, ${g}, ${b}, ${d.alpha * d.life})`;
            ctx.fill();
        }

        // ---- Ripples ----
        for (let i = ripples.length - 1; i >= 0; i--) {
            const rp = ripples[i];
            rp.r += rp.speed;
            rp.alpha -= 0.015;
            if (rp.alpha <= 0 || rp.r > rp.maxR) {
                ripples.splice(i, 1);
                continue;
            }
            ctx.beginPath();
            ctx.ellipse(rp.x, rp.y, rp.r, rp.r * 0.35, 0, 0, Math.PI * 2);
            ctx.strokeStyle = `rgba(100, 60, 20, ${rp.alpha})`;
            ctx.lineWidth = 1.5;
            ctx.stroke();
        }

        // ---- Coffee pool at bottom of stream ----
        if (streamPoints.length > 5 && scrollProgress > 0.15) {
            const last = streamPoints[streamPoints.length - 1];
            const poolRadius = (scrollProgress - 0.15) * 80 + 10;
            const poolGrad = ctx.createRadialGradient(last.x, last.y, 0, last.x, last.y, poolRadius);
            poolGrad.addColorStop(0, `rgba(60, 30, 5, ${scrollProgress * 0.5})`);
            poolGrad.addColorStop(0.6, `rgba(80, 45, 15, ${scrollProgress * 0.25})`);
            poolGrad.addColorStop(1, 'rgba(80, 40, 10, 0)');

            ctx.beginPath();
            ctx.ellipse(last.x, last.y, poolRadius, poolRadius * 0.4, 0, 0, Math.PI * 2);
            ctx.fillStyle = poolGrad;
            ctx.fill();
        }

        // ---- Ambient floating particles (coffee aroma) ----
        if (scrollProgress > 0.05 && frameCount % 3 === 0 && drops.length < MAX_DROPS) {
            createDrop(scrollProgress);
        }

        requestAnimationFrame(draw);
    }

    draw();
})();
