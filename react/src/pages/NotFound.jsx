import React from 'react';
import { useNavigate } from 'react-router-dom';
import Bouton from '@components/commons/Bouton';
import Lien from '@components/commons/Lien';
import colors from '@assets/color/color';

/**
 * NotFound - Page 404 élégante
 */
const NotFound = () => {
  const navigate = useNavigate();

  const containerStyle = {
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    fontFamily: "'EB Garamond', Georgia, serif",
    background: `linear-gradient(135deg, ${colors.background.main} 0%, ${colors.white} 50%, ${colors.gray.light} 100%)`,
    padding: '2rem',
    textAlign: 'center'
  };

  const contentStyle = {
    maxWidth: '500px',
    animation: 'fadeInUp 0.6s ease-out forwards'
  };

  const errorCodeStyle = {
    fontSize: '8rem',
    fontWeight: 700,
    background: `linear-gradient(135deg, ${colors.action} 0%, ${colors.primary} 100%)`,
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
    backgroundClip: 'text',
    lineHeight: 1,
    marginBottom: '1rem',
    textShadow: 'none'
  };

  const titleStyle = {
    fontSize: '2rem',
    fontWeight: 600,
    color: colors.text.primary,
    marginBottom: '1rem'
  };

  const descriptionStyle = {
    fontSize: '1.1rem',
    color: colors.text.secondary,
    lineHeight: 1.6,
    marginBottom: '2rem'
  };

  const illustrationStyle = {
    marginBottom: '2rem'
  };

  const buttonsStyle = {
    display: 'flex',
    gap: '1rem',
    justifyContent: 'center',
    flexWrap: 'wrap'
  };

  const keyframesStyle = `
    @keyframes fadeInUp {
      0% {
        opacity: 0;
        transform: translateY(30px);
      }
      100% {
        opacity: 1;
        transform: translateY(0);
      }
    }
    
    @keyframes float {
      0%, 100% {
        transform: translateY(0);
      }
      50% {
        transform: translateY(-10px);
      }
    }
  `;

  return (
    <div style={containerStyle}>
      <style>{keyframesStyle}</style>
      <div style={contentStyle}>
        {/* Illustration */}
        <div style={illustrationStyle}>
          <svg 
            width="200" 
            height="150" 
            viewBox="0 0 200 150" 
            style={{ animation: 'float 3s ease-in-out infinite' }}
          >
            {/* Nuage/Document perdu */}
            <ellipse cx="100" cy="120" rx="80" ry="20" fill={colors.gray.light} opacity="0.5"/>
            <rect x="60" y="30" width="80" height="100" rx="8" fill={colors.white} stroke={colors.gray.medium} strokeWidth="2"/>
            <rect x="70" y="45" width="60" height="6" rx="3" fill={colors.gray.light}/>
            <rect x="70" y="58" width="45" height="6" rx="3" fill={colors.gray.light}/>
            <rect x="70" y="71" width="55" height="6" rx="3" fill={colors.gray.light}/>
            {/* Point d'interrogation */}
            <circle cx="100" cy="100" r="20" fill={colors.action} opacity="0.9"/>
            <text x="100" y="108" textAnchor="middle" fill={colors.white} fontSize="24" fontWeight="bold">?</text>
          </svg>
        </div>

        {/* Code d'erreur */}
        <div style={errorCodeStyle}>404</div>

        {/* Titre */}
        <h1 style={titleStyle}>Page introuvable</h1>

        {/* Description */}
        <p style={descriptionStyle}>
          Oups ! La page que vous recherchez n'existe pas ou a été déplacée. 
          Vérifiez l'URL ou retournez à la page de connexion.
        </p>

        {/* Boutons d'action */}
        <div style={buttonsStyle}>
          <Lien to="/login" variant="filled" size="md">
            Retour à la connexion
          </Lien>
          <Bouton 
            variant="outline" 
            size="md" 
            onClick={() => navigate(-1)}
          >
            Page précédente
          </Bouton>
        </div>
      </div>
    </div>
  );
};

export default NotFound;