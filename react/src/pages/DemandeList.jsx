import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import colors from '@assets/color/color';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const DemandeList = () => {
  const navigate = useNavigate();
  const [searchInput, setSearchInput] = useState('');
  const [demandes, setDemandes] = useState([]);
  const [demandeur, setDemandeur] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [hasSearched, setHasSearched] = useState(false);

  // Effectuer la recherche par numéro de passeport (PASSXXXX) ou ID demande (XXXX)
  const handleSearch = async (e) => {
    e.preventDefault();
    const trimmed = searchInput.trim();

    if (!trimmed) {
      setError('Veuillez saisir un numéro (ex: PASS12345 ou 123)');
      return;
    }

    setLoading(true);
    setError('');
    setDemandes([]);
    setDemandeur(null);
    setHasSearched(true);

    try {
      const response = await fetch(`${API_URL}/demandeur-visa/search?numero=${encodeURIComponent(trimmed)}`);
      const payload = await response.json();

      if (!response.ok || payload.status !== 'success') {
        throw new Error(payload?.error || payload?.message || 'Erreur serveur');
      }

      if (payload.data) {
        setDemandeur(payload.data.demandeur);
        setDemandes(payload.data.demandes || []);
      } else {
        setError('Aucune demande trouvée');
        setDemandes([]);
      }
    } catch (err) {
      setError(err.message || 'Erreur lors de la recherche');
      setDemandes([]);
    } finally {
      setLoading(false);
    }
  };

  const handleViewFiche = (id) => {
    navigate(`/demande-fiche/${id}`);
  };

  const containerStyle = {
    maxWidth: '1000px',
    margin: '0 auto',
    padding: '2rem',
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif'
  };

  const headerStyle = {
    marginBottom: '2rem'
  };

  const titleStyle = {
    fontSize: '1.8rem',
    fontWeight: 700,
    color: colors.text.primary,
    marginBottom: '1rem',
    margin: 0
  };

  const subtitleStyle = {
    fontSize: '0.95rem',
    color: colors.text.secondary,
    marginBottom: '1.5rem'
  };

  const formStyle = {
    display: 'flex',
    gap: '0.75rem',
    marginBottom: '2rem'
  };

  const inputStyle = {
    flex: 1,
    padding: '0.75rem 1rem',
    fontSize: '0.95rem',
    border: `1px solid ${colors.gray.light}`,
    borderRadius: '8px',
    outline: 'none',
    transition: 'border-color 0.2s'
  };

  const buttonStyle = {
    padding: '0.75rem 1.5rem',
    backgroundColor: colors.primary || '#25DB8E',
    color: 'white',
    border: 'none',
    borderRadius: '8px',
    cursor: 'pointer',
    fontWeight: 600,
    transition: 'background-color 0.2s'
  };

  const errorStyle = {
    padding: '1rem',
    backgroundColor: '#fee2e2',
    color: '#dc2626',
    borderRadius: '8px',
    marginBottom: '1rem'
  };

  const loadingStyle = {
    textAlign: 'center',
    padding: '2rem',
    color: colors.text.secondary
  };

  const emptyStyle = {
    textAlign: 'center',
    padding: '2rem',
    color: colors.text.secondary
  };

  const demandeurInfoStyle = {
    backgroundColor: colors.white,
    padding: '1.5rem',
    borderRadius: '8px',
    border: `1px solid ${colors.gray.light}`,
    marginBottom: '1.5rem',
    boxShadow: '0 2px 8px rgba(0,0,0,0.05)'
  };

  const demandeurNameStyle = {
    fontSize: '1.2rem',
    fontWeight: 600,
    color: colors.text.primary,
    marginBottom: '0.5rem'
  };

  const demandeurDetailsStyle = {
    fontSize: '0.9rem',
    color: colors.text.secondary
  };

  const listStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem'
  };

  const itemStyle = {
    backgroundColor: colors.white,
    padding: '1.2rem',
    borderRadius: '8px',
    border: `1px solid ${colors.gray.light}`,
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    transition: 'all 0.2s',
  };

  const infoStyle = {
    flex: 1
  };

  const idStyle = {
    fontSize: '1rem',
    fontWeight: 600,
    color: colors.text.primary,
    marginBottom: '0.3rem'
  };

  const dateStyle = {
    fontSize: '0.85rem',
    color: colors.text.secondary,
    marginBottom: '0.3rem'
  };

  const typeStyle = {
    fontSize: '0.85rem',
    color: colors.text.secondary
  };

  const linkButtonStyle = {
    padding: '0.5rem 1rem',
    backgroundColor: colors.primary || '#25DB8E',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '0.85rem',
    fontWeight: 600,
    transition: 'background-color 0.2s'
  };

  const helpTextStyle = {
    fontSize: '0.85rem',
    color: colors.text.secondary,
    marginTop: '0.5rem',
    fontStyle: 'italic'
  };

  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <h1 style={titleStyle}>Recherche de Demandes Visa</h1>
        <p style={subtitleStyle}>
          Entrez un numéro de passeport (format: PASSXXXX) ou un numéro de demande pour voir la liste de demandes
        </p>
        
        <form onSubmit={handleSearch} style={formStyle}>
          <input
            type="text"
            placeholder="Ex: PASS123456 ou 789"
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            style={inputStyle}
            onFocus={(e) => e.target.style.borderColor = colors.primary || '#25DB8E'}
            onBlur={(e) => e.target.style.borderColor = colors.gray.light}
          />
          <button
            type="submit"
            style={buttonStyle}
            onMouseEnter={(e) => e.target.style.backgroundColor = '#1ea976'}
            onMouseLeave={(e) => e.target.style.backgroundColor = colors.primary || '#25DB8E'}
          >
            Rechercher
          </button>
        </form>
        <div style={helpTextStyle}>
          💡 Format accepté: PASSXXXX (passeport) ou simplement XXXX (numéro de demande)
        </div>
      </div>

      {error && <div style={errorStyle}>{error}</div>}

      {loading ? (
        <div style={loadingStyle}>Recherche en cours...</div>
      ) : hasSearched && demandes.length === 0 && !error ? (
        <div style={emptyStyle}>Aucune demande trouvée pour cette recherche</div>
      ) : demandes.length > 0 ? (
        <>
          {demandeur && (
            <div style={demandeurInfoStyle}>
              <div style={demandeurNameStyle}>
                {demandeur.nom} {demandeur.prenom}
              </div>
              <div style={demandeurDetailsStyle}>
                Nationalité: {demandeur.nationalite || '---'} | 
                Téléphone: {demandeur.telephone || '---'}
              </div>
            </div>
          )}

          <h2 style={{ fontSize: '1.2rem', color: colors.text.primary, marginBottom: '1rem' }}>
            Demandes ({demandes.length})
          </h2>

          <div style={listStyle}>
            {demandes.map((demande) => (
              <div
                key={demande.id}
                style={itemStyle}
                onMouseEnter={(e) => Object.assign(e.currentTarget.style, { boxShadow: '0 4px 12px rgba(0,0,0,0.1)', transform: 'translateY(-2px)' })}
                onMouseLeave={(e) => Object.assign(e.currentTarget.style, { boxShadow: 'none', transform: 'translateY(0)' })}
              >
                <div style={infoStyle}>
                  <div style={idStyle}>Demande #{demande.id}</div>
                  <div style={dateStyle}>{demande.dateDemande}</div>
                  <div style={typeStyle}>
                    Type: {demande.typeDemande?.libelle || 'Non spécifié'} | 
                    Visa: {demande.visaType || 'Non spécifié'}
                  </div>
                </div>
                <button
                  onClick={() => handleViewFiche(demande.id)}
                  style={linkButtonStyle}
                  onMouseEnter={(e) => e.target.style.backgroundColor = '#1ea976'}
                  onMouseLeave={(e) => e.target.style.backgroundColor = colors.primary || '#25DB8E'}
                >
                  Voir fiche
                </button>
              </div>
            ))}
          </div>
        </>
      ) : !hasSearched ? (
        <div style={emptyStyle}>
          Saisissez un numéro de passeport (PASSXXXX) ou un numéro de demande pour commencer
        </div>
      ) : null}
    </div>
  );
};

export default DemandeList;
