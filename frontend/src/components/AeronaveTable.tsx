import { useState } from 'react'
import type { AeronaveResponse, Marca } from '../types/aeronave'

interface Props {
  aeronaves: AeronaveResponse[]
  loading: boolean
  onToggleVendido: (a: AeronaveResponse) => Promise<void>
  onDelete: (id: number) => Promise<void>
  onEdit: (a: AeronaveResponse) => void
  onError: (msg: string) => void
}

const marcaLabel = (m: Marca) => m.charAt(0) + m.slice(1).toLowerCase()

export function AeronaveTable({ aeronaves, loading, onToggleVendido, onDelete, onEdit, onError }: Props) {
  const [search, setSearch] = useState('')

  const filtered = aeronaves.filter((a) => {
    const q = search.toLowerCase()
    return a.nome.toLowerCase().includes(q) || String(a.id).includes(q)
  })

  const handleDelete = async (id: number) => {
    try { await onDelete(id) }
    catch { onError('Erro ao remover aeronave.') }
  }

  const handleToggle = async (a: AeronaveResponse) => {
    try { await onToggleVendido(a) }
    catch { onError('Erro ao atualizar status de venda.') }
  }

  return (
    <div className="card card--table">
      <div className="table-header">
        <div className="search-wrapper">
          <span className="search-icon">⌕</span>
          <input
            type="text"
            placeholder="Pesquisa por Modelo ou ID"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      {loading ? (
        <div className="empty"><span className="spinner" />Carregando aeronaves...</div>
      ) : filtered.length === 0 ? (
        <div className="empty">
          {search ? 'Nenhum resultado para a busca.' : 'Nenhuma aeronave cadastrada.'}
        </div>
      ) : (
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Ano</th>
                <th className="center">Vendido</th>
                <th className="center">Editar</th>
                <th className="center">Excluir</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((a) => (
                <tr key={a.id}>
                  <td><span className="id-badge">#{a.id}</span></td>
                  <td>
                    <span className={`marca-tag marca-tag--${a.marca}`}>
                      {marcaLabel(a.marca)}
                    </span>
                  </td>
                  <td>{a.nome}</td>
                  <td>{a.ano}</td>
                  <td className="center">
                    <button
                      className={`vendido-btn${a.vendido ? ' vendido-btn--sold' : ''}`}
                      onClick={() => handleToggle(a)}
                      title={a.vendido ? 'Marcar como disponível' : 'Marcar como vendido'}
                    />
                  </td>
                  <td className="center">
                    <button className="edit-btn" onClick={() => onEdit(a)} title="Editar">
                      ✎
                    </button>
                  </td>
                  <td className="center">
                    <button className="del-btn" onClick={() => handleDelete(a.id)} title="Excluir">
                      ✕
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
