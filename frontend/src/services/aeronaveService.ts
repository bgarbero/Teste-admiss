import axios from 'axios'
import type { AeronaveRequest, AeronaveResponse, AeronaveResponseFull, PageResponse } from '../types/aeronave'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
})

export const aeronaveService = {
  findAll: (page = 0, size = 100) =>
    api
      .get<PageResponse<AeronaveResponse>>('/aeronaves', { params: { page, size, sort: 'id' } })
      .then((r) => r.data),

  findById: (id: number) =>
    api.get<AeronaveResponseFull>(`/aeronaves/${id}`).then((r) => r.data),

  insert: (dto: AeronaveRequest) =>
    api.post<AeronaveResponseFull>('/aeronaves', dto).then((r) => r.data),

  update: (dto: AeronaveRequest) =>
    api.put<AeronaveResponse>('/aeronaves', dto).then((r) => r.data),

  remove: (id: number) =>
    api.delete(`/aeronaves/${id}`).then((r) => r.data),
}
