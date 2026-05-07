import React from 'react';
import { Routes, Route } from 'react-router-dom';

import DemandeList from '@/pages/DemandeList';
import DemandeFiche from '@/pages/DemandeFiche';
import NotFound from '@pages/NotFound';

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<DemandeList />} />
      <Route path="/demande-fiche/:id" element={<DemandeFiche />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
