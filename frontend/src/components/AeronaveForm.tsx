import { useState } from 'react'
import type { AeronaveRequest, Marca } from '../types/aeronave'

const MARCAS: Marca[] = ['EMBRAER', 'BOEING', 'AIRBUS']

interface Props {
  onSubmit: (dto: AeronaveRequest) => Promise<void>
  onError: (msg: string) => void
  onSuccess: (msg: string) => void
}

interface FormErrors {
  marca?: string
  nome?: string
  ano?: string
  descricao?: string
  vendido?: string
}

export function AeronaveForm({ onSubmit, onError, onSuccess }: Props) {
  const [marca, setMarca] = useState<Marca | ''>('')
  const [nome, setNome] = useState('')
  const [ano, setAno] = useState('')
  const [descricao, setDescricao] = useState('')
  const [vendido, setVendido] = useState<'' | 'true' | 'false'>('')
  const [errors, setErrors] = useState<FormErrors>({})
  const [submitting, setSubmitting] = useState(false)

  const validate = (): boolean => {
    const errs: FormErrors = {}
    if (!marca) errs.marca = 'Selecione uma marca'
    if (!nome || nome.trim().length < 3) errs.nome = 'Mínimo 3 caracteres'
    else if (nome.trim().length > 70) errs.nome = 'Máximo 70 caracteres'
    const anoNum = parseInt(ano)
    if (!ano || isNaN(anoNum)) errs.ano = 'Ano obrigatório'
    else if (anoNum < 1900) errs.ano = 'Ano mínimo: 1900'
    else if (anoNum > new Date().getFullYear()) errs.ano = 'Ano não pode ser futuro'
    if (!descricao || descricao.trim().length < 3) errs.descricao = 'Mínimo 3 caracteres'
    else if (descricao.trim().length > 70) errs.descricao = 'Máximo 70 caracteres'
    if (vendido === '') errs.vendido = 'Selecione o status'
    setErrors(errs)
    return Object.keys(errs).length === 0
  }

  const handleSubmit = async () => {
    if (!validate()) return
    setSubmitting(true)
    try {
      await onSubmit({
        nome: nome.trim(),
        marca: marca as Marca,
        ano: parseInt(ano),
        descricao: descricao.trim(),
        vendido: vendido === 'true',
      })
      onSuccess('Aeronave cadastrada com sucesso!')
      setMarca(''); setNome(''); setAno(''); setDescricao(''); setVendido(''); setErrors({})
    } catch {
      onError('Erro ao cadastrar. Verifique os dados e tente novamente.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="card">
      <p className="card-title">Nova Aeronave</p>
      <div className="form-grid">

        <div className="form-group">
          <label>Marca</label>
          <div className="select-wrapper">
            <select value={marca} onChange={(e) => setMarca(e.target.value as Marca | '')}>
              <option value="">Selecione a marca</option>
              {MARCAS.map((m) => (
                <option key={m} value={m}>{m.charAt(0) + m.slice(1).toLowerCase()}</option>
              ))}
            </select>
          </div>
          {errors.marca && <span className="field-error">{errors.marca}</span>}
        </div>

        <div className="form-group">
          <label>Modelo / Nome</label>
          <input
            type="text"
            placeholder="Ex: E2-190"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
          />
          {errors.nome && <span className="field-error">{errors.nome}</span>}
        </div>

        <div className="form-group">
          <label>Ano de Fabricação</label>
          <input
            type="number"
            placeholder="Ex: 2014"
            value={ano}
            onChange={(e) => setAno(e.target.value)}
          />
          {errors.ano && <span className="field-error">{errors.ano}</span>}
        </div>

        <div className="form-group">
          <label>Status de Venda</label>
          <div className="select-wrapper">
            <select value={vendido} onChange={(e) => setVendido(e.target.value as '' | 'true' | 'false')}>
              <option value="">Selecione</option>
              <option value="false">Disponível</option>
              <option value="true">Vendido</option>
            </select>
          </div>
          {errors.vendido && <span className="field-error">{errors.vendido}</span>}
        </div>

        <div className="form-group full">
          <label>Descrição</label>
          <input
            type="text"
            placeholder="Descrição da aeronave"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />
          {errors.descricao && <span className="field-error">{errors.descricao}</span>}
        </div>

      </div>
      <div className="btn-row">
        <button className="btn btn-primary" onClick={handleSubmit} disabled={submitting}>
          {submitting ? 'Gravando...' : '✦ Gravar'}
        </button>
      </div>
    </div>
  )
}
