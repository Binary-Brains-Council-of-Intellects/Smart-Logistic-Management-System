import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { RouterProvider } from 'react-router-dom'
import Router from './router/Router.jsx'
import { SLMSProvider } from './context/SLMSContext.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <SLMSProvider>
      <RouterProvider router={Router} />
    </SLMSProvider>
  </StrictMode>,
)
