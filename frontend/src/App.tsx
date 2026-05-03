import { useState } from 'react'
import { useAeronaves } from './hooks/useAeronaves'
import { AeronaveForm } from './components/AeronaveForm'
import { AeronaveTable } from './components/AeronaveTable'
import { StatsPanel } from './components/StatsPanel'
import { Toast } from './components/Toast'
import { EditModal } from './components/EditModal'
import type { AeronaveResponse } from './types/aeronave'

function App() {
  const { aeronaves, loading, error, insert, update, toggleVendido, remove } = useAeronaves()
  const [toast, setToast] = useState<{ msg: string; type: 'ok' | 'err' } | null>(null)
  const [editing, setEditing] = useState<AeronaveResponse | null>(null)

  const showToast = (msg: string, type: 'ok' | 'err') => {
    setToast({ msg, type })
    setTimeout(() => setToast(null), 3000)
  }

  return (
    <>
      <div className="app">
        <header className="header">
          <div className="header-icon">✈</div>
          <div className="header-text">
            <h1>Gestão de Aeronaves</h1>
            <p>Sistema de controle de frota</p>
          </div>
        </header>

        <AeronaveForm
          onSubmit={insert}
          onSuccess={(msg) => showToast(msg, 'ok')}
          onError={(msg) => showToast(msg, 'err')}
        />

        {error && <div className="error-banner">⚠ {error}</div>}

        <StatsPanel aeronaves={aeronaves} />

        <AeronaveTable
          aeronaves={aeronaves}
          loading={loading}
          onToggleVendido={toggleVendido}
          onDelete={remove}
          onEdit={(a) => setEditing(a)}
          onError={(msg) => showToast(msg, 'err')}
        />
      </div>

      <EditModal
        aeronave={editing}
        onClose={() => setEditing(null)}
        onSubmit={update}
        onSuccess={(msg) => showToast(msg, 'ok')}
        onError={(msg) => showToast(msg, 'err')}
      />

      {toast && <Toast message={toast.msg} type={toast.type} />}
    </>
  )
}

export default App
