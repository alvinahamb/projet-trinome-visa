import { Routes, Route, useNavigate } from 'react-router-dom';
import Layout from '@components/layouts/Layout';
import MenuAchat from '@pages/achat/MenuAchat';

const achatNavItems = [
  {
    label: 'Commandes',
    to: '/achat/commandes',
    badge: ''
  },
  {
    label: 'Fournisseurs',
    to: '/achat/fournisseurs',
    badge: ''
  }
];

const AchatRoutes = () => {
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
            title="Gestion des Achats"
            navItems={achatNavItems}
            onLogout={handleLogout}
          >
            <MenuAchat />
          </Layout>
        }
      />
    </Routes>
  );
};

export default AchatRoutes;