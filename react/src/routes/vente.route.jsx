import { Routes, Route, useNavigate } from 'react-router-dom';
import Layout from '@components/layouts/Layout';
import MenuVente from '@pages/vente/MenuVente';

const venteNavItems = [
  {
    label: 'Factures',
    to: '/vente/factures',
    badge: ''
  },
  {
    label: 'Clients',
    to: '/vente/clients',
    badge: ''
  }
];

const VenteRoutes = () => {
  const navigate = useNavigate();
  
  const handleLogout = () => {
    navigate('/login');
  };

  return (
    <Routes>    
      <Route 
        index 
        element={
          <Layout
            title="Gestion des Ventes"
            navItems={venteNavItems}
            onLogout={handleLogout}
          >
            <MenuVente />
          </Layout>
        }
      />
    </Routes>
  );
};

export default VenteRoutes;