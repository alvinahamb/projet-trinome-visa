import React, { useEffect, useRef, useState } from 'react';
import Header from '@components/layouts/Header';
import Footer from '@components/layouts/Footer';
import colors from '@assets/color/color';

/**
 * Layout - Mise en page principale avec animation de transition
 * @param {node} children - Contenu de la page
 * @param {string} title - Titre dans le header
 * @param {string} logo - URL du logo
 * @param {array} navItems - Éléments de navigation
 * @param {function} onLogout - Fonction de déconnexion
 * @param {boolean} showLogout - Afficher le bouton déconnexion
 */
const Layout = ({ 
  children, 
  title = '',
  logo = null,
  navItems = [],
  onLogout,
  showLogout = true
}) => {
  const mainRef = useRef(null);
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    // Animation d'entrée sophistiquée
    setIsVisible(false);
    
    const timer = setTimeout(() => {
      setIsVisible(true);
      
      if (mainRef.current) {
        // Animation de particules en arrière-plan
        createParticleEffect(mainRef.current);
      }
    }, 50);

    return () => clearTimeout(timer);
  }, [children]);

  // Créer un effet de particules moderne
  const createParticleEffect = (container) => {
    const particles = [];
    const particleCount = 6;
    
    for (let i = 0; i < particleCount; i++) {
      const particle = document.createElement('div');
      particle.style.cssText = `
        position: absolute;
        width: ${Math.random() * 10 + 5}px;
        height: ${Math.random() * 10 + 5}px;
        background: linear-gradient(135deg, ${colors.primary}30, ${colors.action}20);
        border-radius: 50%;
        pointer-events: none;
        opacity: 0;
        left: ${Math.random() * 100}%;
        top: ${Math.random() * 100}%;
        animation: particleFade 1.5s ease-out forwards;
        animation-delay: ${i * 0.1}s;
      `;
      container.appendChild(particle);
      particles.push(particle);
    }

    // Nettoyer les particules après l'animation
    setTimeout(() => {
      particles.forEach(p => p.remove());
    }, 2000);
  };

  const layoutStyle = {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
    fontFamily: "'EB Garamond', Georgia, serif"
  };

  const mainStyle = {
    flex: 1,
    position: 'relative',
    overflow: 'hidden',
    // Background moderne et design pour un SI
    background: `
      linear-gradient(135deg, ${colors.background.main} 0%, ${colors.white} 50%, ${colors.gray.light} 100%)
    `,
    // Effet de grille subtile
    backgroundImage: `
      linear-gradient(135deg, ${colors.background.main} 0%, ${colors.white} 50%, ${colors.gray.light} 100%),
      linear-gradient(90deg, transparent 0%, transparent 49%, ${colors.gray.medium}10 50%, transparent 51%, transparent 100%),
      linear-gradient(0deg, transparent 0%, transparent 49%, ${colors.gray.medium}10 50%, transparent 51%, transparent 100%)
    `,
    backgroundSize: '100% 100%, 60px 60px, 60px 60px'
  };

  const contentWrapperStyle = {
    opacity: isVisible ? 1 : 0,
    transform: isVisible ? 'translateX(0)' : 'translateX(-40px)',
    transition: 'all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
    padding: '2rem',
    maxWidth: '1400px',
    margin: '0 auto',
    width: '100%'
  };

  // Styles pour les animations de particules
  const keyframesStyle = `
    @keyframes particleFade {
      0% {
        opacity: 0;
        transform: scale(0) translateY(20px);
      }
      50% {
        opacity: 0.6;
        transform: scale(1.2) translateY(-10px);
      }
      100% {
        opacity: 0;
        transform: scale(0.8) translateY(-30px);
      }
    }
    
    @keyframes shimmer {
      0% {
        background-position: -200% 0;
      }
      100% {
        background-position: 200% 0;
      }
    }
    
    @keyframes floatIn {
      0% {
        opacity: 0;
        transform: translateY(20px) scale(0.95);
      }
      100% {
        opacity: 1;
        transform: translateY(0) scale(1);
      }
    }
  `;

  // Élément décoratif en arrière-plan
  const decorativeElementStyle = {
    position: 'absolute',
    top: '-50%',
    right: '-20%',
    width: '600px',
    height: '600px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.primary}08 0%, transparent 70%)`,
    pointerEvents: 'none',
    zIndex: 0
  };

  const decorativeElement2Style = {
    position: 'absolute',
    bottom: '-30%',
    left: '-10%',
    width: '400px',
    height: '400px',
    borderRadius: '50%',
    background: `radial-gradient(circle, ${colors.action}05 0%, transparent 70%)`,
    pointerEvents: 'none',
    zIndex: 0
  };

  return (
    <div style={layoutStyle}>
      <style>{keyframesStyle}</style>
      
      <Header 
        title={title}
        logo={logo}
        navItems={navItems}
        onLogout={onLogout}
        showLogout={showLogout}
      />
      
      <main style={mainStyle} ref={mainRef}>
        {/* Éléments décoratifs */}
        <div style={decorativeElementStyle}></div>
        <div style={decorativeElement2Style}></div>
        
        {/* Contenu avec animation */}
        <div style={contentWrapperStyle}>
          {children}
        </div>
      </main>
      
      <Footer />
    </div>
  );
};

export default Layout;