import React, { useState, useRef, useEffect } from 'react';
import colors from '@assets/color/color';

/**
 * Popover - Composant div flottant avec liste
 * @param {node} trigger - Élément déclencheur
 * @param {array} items - Liste des éléments à afficher
 * @param {string} position - Position: 'top', 'bottom', 'left', 'right'
 * @param {function} onItemClick - Fonction appelée au clic sur un item
 */
const Popover = ({ 
  trigger, 
  items = [], 
  position = 'bottom',
  onItemClick,
  style = {}
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const popoverRef = useRef(null);

  // Fermer le popover si on clique en dehors
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (popoverRef.current && !popoverRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const getPositionStyles = () => {
    const positions = {
      top: {
        bottom: '100%',
        left: '50%',
        transform: 'translateX(-50%)',
        marginBottom: '8px'
      },
      bottom: {
        top: '100%',
        left: '50%',
        transform: 'translateX(-50%)',
        marginTop: '8px'
      },
      left: {
        right: '100%',
        top: '50%',
        transform: 'translateY(-50%)',
        marginRight: '8px'
      },
      right: {
        left: '100%',
        top: '50%',
        transform: 'translateY(-50%)',
        marginLeft: '8px'
      }
    };
    return positions[position];
  };

  const containerStyle = {
    position: 'relative',
    display: 'inline-block',
    ...style
  };

  const popoverStyle = {
    position: 'absolute',
    ...getPositionStyles(),
    backgroundColor: colors.white,
    borderRadius: '12px',
    boxShadow: '0 10px 40px rgba(0, 0, 0, 0.15)',
    border: `1px solid ${colors.gray.medium}`,
    zIndex: 1000,
    minWidth: '180px',
    overflow: 'hidden',
    opacity: isOpen ? 1 : 0,
    visibility: isOpen ? 'visible' : 'hidden',
    transition: 'all 0.25s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
    fontFamily: "'EB Garamond', Georgia, serif"
  };

  const itemStyle = {
    padding: '0.875rem 1.25rem',
    cursor: 'pointer',
    transition: 'all 0.2s ease',
    color: colors.text.primary,
    fontSize: '1rem',
    borderBottom: `1px solid ${colors.gray.light}`,
    display: 'flex',
    alignItems: 'center',
    gap: '0.75rem'
  };

  const handleItemClick = (item, index) => {
    if (onItemClick) {
      onItemClick(item, index);
    }
    setIsOpen(false);
  };

  return (
    <div style={containerStyle} ref={popoverRef}>
      <div 
        onClick={() => setIsOpen(!isOpen)}
        style={{ cursor: 'pointer' }}
      >
        {trigger}
      </div>
      <div style={popoverStyle}>
        {items.map((item, index) => (
          <div
            key={index}
            style={{
              ...itemStyle,
              borderBottom: index === items.length - 1 ? 'none' : itemStyle.borderBottom
            }}
            onClick={() => handleItemClick(item, index)}
            onMouseEnter={(e) => {
              e.target.style.backgroundColor = colors.gray.light;
              e.target.style.color = colors.action;
            }}
            onMouseLeave={(e) => {
              e.target.style.backgroundColor = 'transparent';
              e.target.style.color = colors.text.primary;
            }}
          >
            {item.icon && <span>{item.icon}</span>}
            <span>{item.label || item}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Popover;