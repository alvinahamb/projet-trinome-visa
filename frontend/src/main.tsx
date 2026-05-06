import { createRoot } from 'react-dom/client'
import './index.css'
import { mainRoutes } from './routes/mainRoutes.ts';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { Toaster } from './components/ui/sonner.tsx';

const router = createBrowserRouter(mainRoutes);

createRoot(document.getElementById('root')!).render(
  <>
    <Toaster position="top-right" />
    <RouterProvider router={router} />
  </>,
)
