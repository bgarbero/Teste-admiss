import { useState, useEffect } from 'react'
import type { AeronaveResponse, AeronaveRequest, Marca } from '../types/aeronave'

const MARCAS: Marca[] = ['EMBRAER', 'BOEING', 'AIRBUS']

interface Props {
  aeronave: AeronaveResponse | null
  onClose: () => void
  onSubmit: (dto: AeronaveRequest) => Promise<void>
  onSuccess: (msg: string) => void
  onError: (msg: string) => void
}

interface FormErrors {
  nome?: string
  ano?: string
  descricao?: string
}

export function EditModal({ aeronave, onClose, onSubmit, onSuccess, onError }: Props) {
  const [marca, setMarca] = useState<Marca>('EMBRAER')
  const [nome, setNome] = useState('')
  const [ano, setAno] = useState('')
  const [descricao, setDescricao] = useState('')
  const [vendido, setVendido] = useState<'true' | 'false'>('false')
  const [errors, setErrors] = useState<FormErrors>({})
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    if (aeronave) {
      setMarca(aeronave.marca)
      setNome(aeronave.nome)
      setAno(String(aeronave.ano))
      setDescricao('-')          // list DTO não tem descricao; usuário preenche
      setVendido(aeronave.vendido ? 'true' : 'false')
      setErrors({})
    }
  }, [aeronave])

  if (!aeronave) return null

  const validate = (): boolean => {
    const errs: FormErrors = {}
    if (!nome || nome.trim().length < 3) errs.nome = 'Mínimo 3 caracteres'
    else if (nome.trim().length > 70) errs.nome = 'Máximo 70 caracteres'
    const anoNum = parseInt(ano)
    if (!ano || isNaN(anoNum)) errs.ano = 'Ano obrigatório'
    else if (anoNum < 1900) errs.ano = 'Ano mínimo: 1900'
    else if (anoNum > new Date().getFullYear()) errs.ano = 'Ano não pode ser futuro'
    if (!descricao || descricao.trim().length < 3) errs.descricao = 'Mínimo 3 caracteres'
    else if (descricao.trim().length > 70) errs.descricao = 'Máximo 70 caracteres'
    setErrors(errs)
    return Object.keys(errs).length === 0
  }

  const handleSubmit = async () => {
    if (!validate()) return
    setSubmitting(true)
    try {
      await onSubmit({
        id: aeronave.id,
        nome: nome.trim(),
        marca,
        ano: parseInt(ano),
        descricao: descricao.trim(),
        vendido: vendido === 'true',
      })
      onSuccess('Aeronave atualizada com sucesso!')
      onClose()
    } catch {
      onError('Erro ao atualizar aeronave.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <>
      <div className="modal-backdrop" onClick={onClose} />
      <div className="modal">
        <div className="modal-header">
          <p className="modal-title">Editar Aeronave <span className="modal-id">#{aeronave.id}</span></p>
          <button className="modal-close" onClick={onClose} title="Fechar">✕</button>
        </div>

        <div className="form-grid">
          <div className="form-group">
            <label>Marca</label>
            <div className="select-wrapper">
              <select value={marca} onChange={(e) => setMarca(e.target.value as Marca)}>
                {MARCAS.map((m) => (
                  <option key={m} value={m}>{m.charAt(0) + m.slice(1).toLowerCase()}</option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group">
            <label>Modelo / Nome</label>
            <input
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
            />
            {errors.nome && <span className="field-error">{errors.nome}</span>}
          </div>

          <div className="form-group">
            <label>Ano de Fabricação</label>
            <input
              type="number"
              value={ano}
              onChange={(e) => setAno(e.target.value)}
            />
            {errors.ano && <span className="field-error">{errors.ano}</span>}
          </div>

          <div className="form-group">
            <label>Status de Venda</label>
            <div className="select-wrapper">
              <select value={vendido} onChange={(e) => setVendido(e.target.value as 'true' | 'false')}>
                <option value="false">Disponível</option>
                <option value="true">Vendido</option>
              </select>
            </div>
          </div>

          <div className="form-group full">
            <label>Descrição</label>
            <input
              type="text"
              placeholder="Descrição da aeronave"
              value={descricao === '-' ? '' : descricao}
              onChange={(e) => setDescricao(e.target.value)}
            />
            {errors.descricao && <span className="field-error">{errors.descricao}</span>}
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn btn-ghost" onClick={onClose} disabled={submitting}>
            Cancelar
          </button>
          <button className="btn btn-primary" onClick={handleSubmit} disabled={submitting}>
            {submitting ? 'Salvando...' : '✦ Salvar'}
          </button>
        </div>
      </div>
    </>
  )
}
