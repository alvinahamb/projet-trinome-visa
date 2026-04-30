import React, { useState, useRef, useEffect } from 'react';
import colors from '@assets/color/color';

/**
 * Dropdown - Composant de sélection élégant
 * @param {string} label - Nom de l'élément à afficher (ex: "catégorie")
 * @param {array} options - Liste des options [{value, label}]
 * @param {string} value - Valeur sélectionnée
 * @param {function} onChange - Fonction appelée lors du changement
 * @param {string} placeholder - Texte par défaut
 */
const Dropdown = ({ 
  label = 'élément',
  options = [], 
  value, 
  onChange, 
  placeholder,
  disabled = false,
  style = {}
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [minWidth, setMinWidth] = useState(0);
  const dropdownRef = useRef(null);
  const measureRef = useRef(null);

  // Calculer la largeur minimale basée sur le texte le plus long
  useEffect(() => {
    if (measureRef.current) {
      const longestOption = options.reduce((longest, option) => 
        option.label.length > longest.length ? option.label : longest
      , `Choisir un ${label}`);
      
      measureRef.current.textContent = longestOption;
      const width = measureRef.current.offsetWidth;
      setMinWidth(width + 60); // +60 pour padding et icône
    }
  }, [options, label]);

  // Fermer le dropdown si on clique en dehors
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const selectedOption = options.find(opt => opt.value === value);
  const displayText = selectedOption ? selectedOption.label : (placeholder || `Choisir un ${label}`);

  const containerStyle = {
    position: 'relative',
    display: 'inline-block',
    fontFamily: "'EB Garamond', Georgia, serif",
    minWidth: `${minWidth}px`,
    ...style
  };

  const triggerStyle = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '0.75rem 1rem',
    backgroundColor: colors.white,
    border: `2px solid ${isOpen ? colors.primary : colors.gray.medium}`,
    borderRadius: '12px',
    cursor: disabled ? 'not-allowed' : 'pointer',
    opacity: disabled ? 0.6 : 1,
    transition: 'all 0.3s ease',
    fontSize: '1rem',
    color: selectedOption ? colors.text.primary : colors.gray.dark,
    boxShadow: isOpen ? '0 4px 15px rgba(37, 219, 142, 0.15)' : '0 2px 8px rgba(0, 0, 0, 0.05)',
    minWidth: '100%'
  };

  const menuStyle = {
    position: 'absolute',
    top: 'calc(100% + 4px)',
    left: 0,
    right: 0,
    backgroundColor: colors.white,
    border: `1px solid ${colors.gray.medium}`,
    borderRadius: '12px',
    boxShadow: '0 10px 30px rgba(0, 0, 0, 0.12)',
    zIndex: 1000,
    overflow: 'hidden',
    opacity: isOpen ? 1 : 0,
    visibility: isOpen ? 'visible' : 'hidden',
    transform: isOpen ? 'translateY(0)' : 'translateY(-10px)',
    transition: 'all 0.25s cubic-bezier(0.25, 0.46, 0.45, 0.94)'
  };

  const optionStyle = (isSelected) => ({
    padding: '0.75rem 1rem',
    cursor: 'pointer',
    backgroundColor: isSelected ? `${colors.primary}15` : 'transparent',
    color: isSelected ? colors.primary : colors.text.primary,
    fontWeight: isSelected ? 600 : 400,
    transition: 'all 0.2s ease',
    borderBottom: `1px solid ${colors.gray.light}`
  });

  const chevronStyle = {
    width: '12px',
    height: '12px',
    borderRight: `2px solid ${colors.gray.dark}`,
    borderBottom: `2px solid ${colors.gray.dark}`,
    transform: isOpen ? 'rotate(-135deg)' : 'rotate(45deg)',
    transition: 'transform 0.3s ease',
    marginLeft: '0.5rem'
  };

  const measureStyle = {
    position: 'absolute',
    visibility: 'hidden',
    whiteSpace: 'nowrap',
    fontSize: '1rem',
    fontFamily: "'EB Garamond', Georgia, serif"
  };

  const handleSelect = (option) => {
    onChange(option.value);
    setIsOpen(false);
  };

  return (
    <div style={containerStyle} ref={dropdownRef}>
      <span ref={measureRef} style={measureStyle}></span>
      <div 
        style={triggerStyle}
        onClick={() => !disabled && setIsOpen(!isOpen)}
        onMouseEnter={(e) => {
          if (!disabled) e.target.style.borderColor = colors.primary;
        }}
        onMouseLeave={(e) => {
          if (!disabled && !isOpen) e.target.style.borderColor = colors.gray.medium;
        }}
      >
        <span>{displayText}</span>
        <div style={chevronStyle}></div>
      </div>
      <div style={menuStyle}>
        {options.map((option, index) => (
          <div
            key={option.value}
            style={optionStyle(option.value === value)}
            onClick={() => handleSelect(option)}
            onMouseEnter={(e) => {
              if (option.value !== value) {
                e.target.style.backgroundColor = colors.gray.light;
              }
            }}
            onMouseLeave={(e) => {
              if (option.value !== value) {
                e.target.style.backgroundColor = 'transparent';
              }
            }}
          >
            {option.label}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Dropdown;