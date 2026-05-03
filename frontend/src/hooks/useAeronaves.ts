import { useState, useEffect, useCallback } from 'react'
import { aeronaveService } from '../services/aeronaveService'
import type { AeronaveRequest, AeronaveResponse } from '../types/aeronave'

export function useAeronaves() {
  const [aeronaves, setAeronaves] = useState<AeronaveResponse[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const page = await aeronaveService.findAll()
      setAeronaves(page.content)
    } catch {
      setError('Não foi possível conectar ao servidor. Verifique se o backend está rodando na porta 8080.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { load() }, [load])

  const insert = async (dto: AeronaveRequest) => {
    await aeronaveService.insert(dto)
    await load()
  }

  const toggleVendido = async (aeronave: AeronaveResponse) => {
    const updated: AeronaveRequest = { ...aeronave, vendido: !aeronave.vendido, descricao: '-' }
    await aeronaveService.update(updated)
    setAeronaves((prev) =>
      prev.map((a) => (a.id === aeronave.id ? { ...a, vendido: !a.vendido } : a))
    )
  }

  const update = async (dto: AeronaveRequest) => {
    await aeronaveService.update(dto)
    await load()
  }

  const remove = async (id: number) => {
    await aeronaveService.remove(id)
    setAeronaves((prev) => prev.filter((a) => a.id !== id))
  }

  return { aeronaves, loading, error, load, insert, update, toggleVendido, remove }
}
