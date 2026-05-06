import React, { useEffect, useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';
import Layout from '@components/layouts/Layout';
import Lien from '@components/commons/Lien';
import colors from '@assets/color/color';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const DemandeFiche = () => {
  const { id } = useParams();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [fiche, setFiche] = useState(null);

  useEffect(() => {
    let active = true;

    const fetchFiche = async () => {
      setLoading(true);
      setError('');

      try {
        const response = await fetch(`${API_URL}/demande-visa/demande-fiche/${id}`);
        const payload = await response.json();

        if (!response.ok || payload.status !== 'success') {
          throw new Error(payload?.error || payload?.message || 'Erreur serveur');
        }

        if (active) {
          setFiche(payload.data || null);
        }
      } catch (err) {
        if (active) {
          setError(err.message || 'Erreur lors du chargement.');
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    };

    fetchFiche();

    return () => {
      active = false;
    };
  }, [id]);

  const demande = fiche?.demande;
  const historiques = useMemo(() => fiche?.historiques || [], [fiche]);

  const pageStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '2rem'
  };

  const headerStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.4rem'
  };

  const titleStyle = {
    fontSize: '2.3rem',
    fontWeight: 700,
    color: colors.text.primary,
    margin: 0
  };

  const subtitleStyle = {
    fontSize: '1rem',
    color: colors.text.secondary
  };

  const contentGridStyle = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))',
    gap: '1.5rem'
  };

  const cardStyle = {
    backgroundColor: colors.white,
    borderRadius: '18px',
    padding: '1.4rem',
    border: `1px solid ${colors.gray.light}`,
    boxShadow: '0 14px 40px rgba(26, 26, 26, 0.06)'
  };

  const labelStyle = {
    fontSize: '0.85rem',
    textTransform: 'uppercase',
    letterSpacing: '0.08em',
    color: colors.text.secondary,
    marginBottom: '0.4rem'
  };

  const valueStyle = {
    fontSize: '1.05rem',
    color: colors.text.primary,
    fontWeight: 600
  };

  const timelineStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem'
  };

  const timelineItemStyle = {
    display: 'flex',
    gap: '1rem',
    alignItems: 'flex-start'
  };

  const dotStyle = {
    width: '12px',
    height: '12px',
    borderRadius: '50%',
    backgroundColor: colors.action,
    marginTop: '0.35rem'
  };

  const lineStyle = {
    width: '2px',
    backgroundColor: colors.gray.medium,
    marginLeft: '5px',
    marginTop: '0.35rem',
    flex: 1
  };

  const timelineCardStyle = {
    ...cardStyle,
    padding: '1.5rem'
  };

  const helperStyle = {
    fontSize: '0.95rem',
    color: colors.text.secondary
  };

  return (
    <Layout title="Fiche demande">
      <div style={pageStyle}>
        <div style={headerStyle}>
          <h1 style={titleStyle}>Fiche demande #{id}</h1>
          <p style={subtitleStyle}>Details complets et historique des statuts.</p>
          <Lien to="/demande-recherche" variant="text">Retour a la recherche</Lien>
        </div>

        {loading && <div style={helperStyle}>Chargement en cours...</div>}
        {error && <div style={{ ...helperStyle, color: colors.error }}>{error}</div>}

        {demande && (
          <div style={contentGridStyle}>
            <div style={cardStyle}>
              <div style={labelStyle}>Demandeur</div>
              <div style={valueStyle}>{demande.nom} {demande.prenom}</div>
              <div style={labelStyle}>Nationalite</div>
              <div style={valueStyle}>{demande.nationalite || '---'}</div>
              <div style={labelStyle}>Contact</div>
              <div style={valueStyle}>{demande.telephone || demande.email || '---'}</div>
            </div>
            <div style={cardStyle}>
              <div style={labelStyle}>Passeport</div>
              <div style={valueStyle}>{demande.numeroPasseport || '---'}</div>
              <div style={labelStyle}>Delivrance</div>
              <div style={valueStyle}>{demande.dateDelivrance || '---'}</div>
              <div style={labelStyle}>Expiration</div>
              <div style={valueStyle}>{demande.dateExpiration || '---'}</div>
            </div>
            <div style={cardStyle}>
              <div style={labelStyle}>Visa</div>
              <div style={valueStyle}>{demande.visaType || '---'}</div>
              <div style={labelStyle}>Reference</div>
              <div style={valueStyle}>{demande.referenceVisa || '---'}</div>
              <div style={labelStyle}>Statut actuel</div>
              <div style={valueStyle}>{demande.statut || '---'}</div>
            </div>
          </div>
        )}

        <div style={timelineCardStyle}>
          <div style={labelStyle}>Historique des statuts</div>
          {historiques.length === 0 && (
            <div style={helperStyle}>Aucun historique disponible.</div>
          )}
          {historiques.length > 0 && (
            <div style={timelineStyle}>
              {historiques.map((item, index) => (
                <div key={`${item.id}-${index}`} style={timelineItemStyle}>
                  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <span style={dotStyle}></span>
                    {index < historiques.length - 1 && <span style={lineStyle}></span>}
                  </div>
                  <div>
                    <div style={valueStyle}>{item.statut || 'Statut inconnu'}</div>
                    <div style={helperStyle}>{item.commentaire || '---'}</div>
                    <div style={helperStyle}>{item.dateChangement || '---'}</div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </Layout>
  );
};

export default DemandeFiche;
