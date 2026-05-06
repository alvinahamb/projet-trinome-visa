import React from 'react';
import { Routes, Route } from 'react-router-dom';

import Login from '@/pages/Login';
import Menu from '@/pages/Menu';
import Dashboard from '@/pages/Dashboard';
import DemandeRecherche from '@/pages/DemandeRecherche';
import DemandeFiche from '@/pages/DemandeFiche';

import AchatRoutes from './achat.route';
import VenteRoutes from './vente.route';
import StockRoutes from './stock.route';

import NotFound from '@pages/NotFound';

const AppRoutes = () => {

  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />
      <Route path="/menu" element={<Menu />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/demande-recherche" element={<DemandeRecherche />} />
      <Route path="/demande-fiche/:id" element={<DemandeFiche />} />
      <Route path="achat/*" element={<AchatRoutes />} />
      <Route path="vente/*" element={<VenteRoutes />} />
      <Route path="stock/*" element={<StockRoutes />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
