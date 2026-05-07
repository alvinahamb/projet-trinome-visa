import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import colors from '@assets/color/color';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const DemandeFiche = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [fiche, setFiche] = useState(null);

  useEffect(() => {
    const fetchFiche = async () => {
      setLoading(true);
      setError('');

      try {
        const response = await fetch(`${API_URL}/demandeur-visa/demande-fiche/${id}`);
        const payload = await response.json();

        if (!response.ok || payload.status !== 'success') {
          throw new Error(payload?.error || payload?.message || 'Erreur serveur');
        }

        setFiche(payload.data || null);
      } catch (err) {
        setError(err.message || 'Erreur lors du chargement');
      } finally {
        setLoading(false);
      }
    };

    fetchFiche();
  }, [id]);

  const demande = fiche?.demande;
  const historiques = fiche?.historiques || [];

  const containerStyle = {
    maxWidth: '1200px',
    margin: '0 auto',
    padding: '2rem',
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif'
  };

  const headerStyle = {
    marginBottom: '2rem',
    paddingBottom: '1.5rem',
    borderBottom: `1px solid ${colors.gray.light}`
  };

  const titleStyle = {
    fontSize: '2rem',
    fontWeight: 700,
    color: colors.text.primary,
    margin: '0 0 0.5rem 0'
  };

  const backButtonStyle = {
    display: 'inline-block',
    marginTop: '1rem',
    padding: '0.5rem 1rem',
    backgroundColor: colors.gray.light,
    color: colors.text.primary,
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '0.9rem',
    transition: 'background-color 0.2s'
  };

  const gridStyle = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: '1.5rem',
    marginBottom: '2rem'
  };

  const cardStyle = {
    backgroundColor: colors.white,
    padding: '1.5rem',
    borderRadius: '8px',
    border: `1px solid ${colors.gray.light}`,
    boxShadow: '0 2px 8px rgba(0,0,0,0.05)'
  };

  const labelStyle = {
    fontSize: '0.8rem',
    textTransform: 'uppercase',
    letterSpacing: '0.05em',
    color: colors.text.secondary,
    marginBottom: '0.5rem',
    fontWeight: 600
  };

  const valueStyle = {
    fontSize: '1rem',
    color: colors.text.primary,
    fontWeight: 500,
    marginBottom: '1.2rem'
  };

  const lastValueStyle = {
    ...valueStyle,
    marginBottom: 0
  };

  const historyStyle = {
    backgroundColor: colors.white,
    padding: '1.5rem',
    borderRadius: '8px',
    border: `1px solid ${colors.gray.light}`,
    boxShadow: '0 2px 8px rgba(0,0,0,0.05)'
  };

  const historyTitleStyle = {
    fontSize: '1.3rem',
    fontWeight: 700,
    color: colors.text.primary,
    marginBottom: '1.5rem',
    margin: '0 0 1.5rem 0'
  };

  const timelineStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem'
  };

  const timelineItemStyle = {
    display: 'flex',
    gap: '1rem',
    paddingBottom: '1rem',
    borderBottom: `1px solid ${colors.gray.light}`,
    position: 'relative'
  };

  const timelineItemLastStyle = {
    ...timelineItemStyle,
    borderBottom: 'none',
    paddingBottom: 0
  };

  const dotStyle = {
    width: '14px',
    height: '14px',
    borderRadius: '50%',
    backgroundColor: colors.primary || '#2563eb',
    marginTop: '0.2rem',
    flexShrink: 0,
    border: `3px solid ${colors.white}`,
    boxShadow: `0 0 0 2px ${colors.primary || '#2563eb'}`
  };

  const contentStyle = {
    flex: 1
  };

  const statusStyle = {
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

  const commentStyle = {
    fontSize: '0.9rem',
    color: colors.text.secondary,
    fontStyle: 'italic'
  };

  const errorStyle = {
    padding: '1rem',
    backgroundColor: '#fee2e2',
    color: '#dc2626',
    borderRadius: '8px',
    marginBottom: '1.5rem'
  };

  const loadingStyle = {
    textAlign: 'center',
    padding: '3rem',
    color: colors.text.secondary,
    fontSize: '1rem'
  };

  const emptyStyle = {
    textAlign: 'center',
    padding: '2rem',
    color: colors.text.secondary
  };

  if (loading) {
    return <div style={containerStyle}><div style={loadingStyle}>Chargement...</div></div>;
  }

  if (error) {
    return (
      <div style={containerStyle}>
        <div style={errorStyle}>{error}</div>
        <button
          onClick={() => navigate('/')}
          style={backButtonStyle}
          onMouseEnter={(e) => e.target.style.backgroundColor = colors.gray.medium}
          onMouseLeave={(e) => e.target.style.backgroundColor = colors.gray.light}
        >
          ← Retour à la liste
        </button>
      </div>
    );
  }

  if (!demande) {
    return (
      <div style={containerStyle}>
        <div style={emptyStyle}>Demande non trouvée</div>
        <button
          onClick={() => navigate('/')}
          style={backButtonStyle}
          onMouseEnter={(e) => e.target.style.backgroundColor = colors.gray.medium}
          onMouseLeave={(e) => e.target.style.backgroundColor = colors.gray.light}
        >
          ← Retour à la liste
        </button>
      </div>
    );
  }

  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <h1 style={titleStyle}>Fiche Demande #{id}</h1>
        <button
          onClick={() => navigate('/')}
          style={backButtonStyle}
          onMouseEnter={(e) => e.target.style.backgroundColor = colors.gray.medium}
          onMouseLeave={(e) => e.target.style.backgroundColor = colors.gray.light}
        >
          ← Retour à la liste
        </button>
      </div>

      {/* Infos principales */}
      <div style={gridStyle}>
        {demande.demandeur && (
          <div style={cardStyle}>
            <div style={labelStyle}>Demandeur</div>
            <div style={valueStyle}>{demande.demandeur.nom} {demande.demandeur.prenom}</div>
            <div style={labelStyle}>Nationalité</div>
            <div style={valueStyle}>{demande.demandeur.nationalite || '---'}</div>
            <div style={labelStyle}>Contact</div>
            <div style={lastValueStyle}>{demande.demandeur.telephone || demande.demandeur.email || '---'}</div>
          </div>
        )}

        {demande.passeport && (
          <div style={cardStyle}>
            <div style={labelStyle}>Passeport</div>
            <div style={valueStyle}>{demande.passeport.numeroPasseport || '---'}</div>
            <div style={labelStyle}>Délivrance</div>
            <div style={valueStyle}>{demande.passeport.dateDelivrance || '---'}</div>
            <div style={labelStyle}>Expiration</div>
            <div style={lastValueStyle}>{demande.passeport.dateExpiration || '---'}</div>
          </div>
        )}

        <div style={cardStyle}>
          <div style={labelStyle}>Type de Demande</div>
          <div style={valueStyle}>{demande.typeDemande?.libelle || '---'}</div>
          <div style={labelStyle}>Type de Visa</div>
          <div style={valueStyle}>{demande.typeVisa?.libelle || '---'}</div>
          <div style={labelStyle}>Date Demande</div>
          <div style={lastValueStyle}>{demande.dateDemande || '---'}</div>
        </div>
      </div>

      {/* Historique des statuts */}
      <div style={historyStyle}>
        <h2 style={historyTitleStyle}>Historique des Statuts</h2>
        
        {historiques.length === 0 ? (
          <div style={emptyStyle}>Aucun historique disponible</div>
        ) : (
          <div style={timelineStyle}>
            {historiques.map((item, index) => (
              <div
                key={`${item.id}-${index}`}
                style={index === historiques.length - 1 ? timelineItemLastStyle : timelineItemStyle}
              >
                <div style={dotStyle}></div>
                <div style={contentStyle}>
                  <div style={statusStyle}>{item.statutDemande?.libelle || item.statut || 'Statut inconnu'}</div>
                  <div style={dateStyle}>
                    {new Date(item.dateStatut).toLocaleString('fr-FR')}
                  </div>
                  {item.commentaire && (
                    <div style={commentStyle}>"{item.commentaire}"</div>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default DemandeFiche;
