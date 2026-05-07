import React, { useMemo, useState } from 'react';
import Layout from '@components/layouts/Layout';
import Bouton from '@components/commons/Bouton';
import Lien from '@components/commons/Lien';
import colors from '@assets/color/color';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const DemandeRecherche = () => {
  const [numero, setNumero] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [result, setResult] = useState(null);
  const [message, setMessage] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    const trimmed = numero.trim();

    if (!trimmed) {
      setError('Veuillez saisir un numero (ex: PASS12 ou 12).');
      return;
    }

    setLoading(true);
    setError('');
    setMessage('');
    setResult(null);

    try {
      const response = await fetch(
        `${API_URL}/demandeur-visa/demandes?numero=${encodeURIComponent(trimmed)}`
      );
      const payload = await response.json();

      if (!response.ok || payload.status !== 'success') {
        throw new Error(payload?.error || payload?.message || 'Erreur serveur');
      }

      if (!payload.data) {
        setMessage(payload.message || 'Aucun demandeur trouve.');
        setResult(null);
        return;
      }

      setResult(payload.data);
      setMessage(payload.message || 'Resultats charges.');
    } catch (err) {
      setError(err.message || 'Erreur lors de la recherche.');
    } finally {
      setLoading(false);
    }
  };

  const demandes = useMemo(() => result?.demandes || [], [result]);
  const demandeur = result?.demandeur;

  const pageStyle = {
    display: 'flex',
    flexDirection: 'column',
    gap: '2rem'
  };

  const headerStyle = {
    display: 'flex',
    alignItems: 'flex-end',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    gap: '1rem'
  };

  const titleStyle = {
    fontSize: '2.4rem',
    fontWeight: 700,
    color: colors.text.primary,
    margin: 0
  };

  const subtitleStyle = {
    fontSize: '1rem',
    color: colors.text.secondary,
    marginTop: '0.35rem'
  };

  const searchCardStyle = {
    backgroundColor: colors.white,
    borderRadius: '20px',
    padding: '1.75rem',
    border: `1px solid ${colors.gray.light}`,
    boxShadow: '0 14px 45px rgba(26, 26, 26, 0.06)'
  };

  const inputRowStyle = {
    display: 'flex',
    flexWrap: 'wrap',
    gap: '1rem',
    alignItems: 'center'
  };

  const inputStyle = {
    flex: 1,
    minWidth: '240px',
    padding: '0.85rem 1rem',
    borderRadius: '12px',
    border: `2px solid ${colors.gray.medium}`,
    fontFamily: "'EB Garamond', Georgia, serif",
    fontSize: '1rem',
    backgroundColor: colors.background.main
  };

  const feedbackStyle = {
    marginTop: '0.75rem',
    fontSize: '0.95rem',
    color: error ? colors.error : colors.text.secondary
  };

  const infoGridStyle = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
    gap: '1rem'
  };

  const infoCardStyle = {
    backgroundColor: colors.white,
    borderRadius: '18px',
    padding: '1.25rem',
    border: `1px solid ${colors.gray.light}`,
    boxShadow: '0 12px 30px rgba(26, 26, 26, 0.05)'
  };

  const infoLabelStyle = {
    fontSize: '0.85rem',
    textTransform: 'uppercase',
    letterSpacing: '0.08em',
    color: colors.text.secondary,
    marginBottom: '0.4rem'
  };

  const infoValueStyle = {
    fontSize: '1.1rem',
    color: colors.text.primary,
    fontWeight: 600
  };

  const demandesGridStyle = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
    gap: '1.5rem'
  };

  const demandeCardStyle = {
    backgroundColor: colors.white,
    borderRadius: '18px',
    padding: '1.5rem',
    border: `1px solid ${colors.gray.light}`,
    boxShadow: '0 16px 40px rgba(26, 26, 26, 0.06)',
    display: 'flex',
    flexDirection: 'column',
    gap: '0.85rem'
  };

  const badgeStyle = (tone) => ({
    alignSelf: 'flex-start',
    padding: '0.35rem 0.8rem',
    borderRadius: '999px',
    fontSize: '0.8rem',
    fontWeight: 600,
    letterSpacing: '0.04em',
    color: tone === 'ok' ? colors.primary : colors.action,
    backgroundColor: tone === 'ok' ? `${colors.primary}15` : `${colors.action}15`
  });

  return (
    <Layout title="Demandeur & Demandes">
      <div style={pageStyle}>
        <div style={headerStyle}>
          <div>
            <h1 style={titleStyle}>Recherche de demande</h1>
            <p style={subtitleStyle}>Entrez un numero de passeport (PASS12) ou un numero de demande.</p>
          </div>
        </div>

        <div style={searchCardStyle}>
          <form onSubmit={handleSubmit}>
            <div style={inputRowStyle}>
              <input
                type="text"
                value={numero}
                onChange={(e) => setNumero(e.target.value)}
                placeholder="PASS12 ou 12"
                style={inputStyle}
              />
              <Bouton type="submit" disabled={loading}>
                {loading ? 'Recherche...' : 'Rechercher'}
              </Bouton>
            </div>
            {(error || message) && (
              <div style={feedbackStyle}>{error || message}</div>
            )}
          </form>
        </div>

        {demandeur && (
          <div style={infoGridStyle}>
            <div style={infoCardStyle}>
              <div style={infoLabelStyle}>Demandeur</div>
              <div style={infoValueStyle}>{demandeur.nom} {demandeur.prenom}</div>
            </div>
            <div style={infoCardStyle}>
              <div style={infoLabelStyle}>Nationalite</div>
              <div style={infoValueStyle}>{demandeur.nationalite || '---'}</div>
            </div>
            <div style={infoCardStyle}>
              <div style={infoLabelStyle}>Contact</div>
              <div style={infoValueStyle}>{demandeur.telephone || demandeur.email || '---'}</div>
            </div>
            <div style={infoCardStyle}>
              <div style={infoLabelStyle}>Adresse</div>
              <div style={infoValueStyle}>{demandeur.adresse || '---'}</div>
            </div>
          </div>
        )}

        {demandeur && (
          <div style={demandesGridStyle}>
            {demandes.map((demande) => (
              <div key={demande.id} style={demandeCardStyle}>
                <span style={badgeStyle(demande.statut === 'TERMINE' ? 'ok' : 'alert')}>
                  {demande.statut || 'Statut inconnu'}
                </span>
                <div style={infoLabelStyle}>Demande ID</div>
                <div style={infoValueStyle}>{demande.id}</div>
                <div style={infoLabelStyle}>Type de visa</div>
                <div style={infoValueStyle}>{demande.visaType || '---'}</div>
                <div style={infoLabelStyle}>Reference visa</div>
                <div style={infoValueStyle}>{demande.referenceVisa || '---'}</div>
                <Lien to={`/demande-fiche/${demande.id}`} variant="outline" size="sm">
                  Voir la fiche
                </Lien>
              </div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
};

export default DemandeRecherche;
