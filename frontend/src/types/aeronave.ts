export type Marca = 'EMBRAER' | 'BOEING' | 'AIRBUS'

export interface AeronaveResponse {
  id: number
  marca: Marca
  nome: string
  ano: number
  vendido: boolean
}

export interface AeronaveResponseFull {
  id: number
  nome: string
  marca: Marca
  ano: number
  descricao: string
  vendido: boolean
  created: string
  updated: string
}

export interface AeronaveRequest {
  id?: number
  nome: string
  marca: Marca
  ano: number
  descricao: string
  vendido: boolean
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}
