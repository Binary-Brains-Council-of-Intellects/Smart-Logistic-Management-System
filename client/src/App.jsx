import { RouterProvider } from 'react-router-dom'
import Router from './router/Router'
import { SLMSProvider } from './context/SLMSContext'

function App() {
  return (
    <SLMSProvider>
      <RouterProvider router={Router} />
    </SLMSProvider>
  )
}

export default App
