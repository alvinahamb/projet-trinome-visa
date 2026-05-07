import { Routes, Route, useNavigate } from 'react-router-dom';
import Layout from '@components/layouts/Layout';
import MenuStock from '@pages/stock/MenuStock';

const stockNavItems = [
  {
    label: 'Inventaire',
    to: '/stock/inventaire',
    badge: ''
  },
  {
    label: 'Mouvements',
    to: '/stock/mouvements',
    badge: ''
  }
];

const StockRoutes = () => {
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
            title="Gestion du Stock"
            navItems={stockNavItems}
            onLogout={handleLogout}
          >
            <MenuStock />
          </Layout>
        }
      />
    </Routes>
  );
};

export default StockRoutes;