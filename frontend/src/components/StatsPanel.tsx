import type { AeronaveResponse } from '../types/aeronave'

interface Props {
  aeronaves: AeronaveResponse[]
}

export function StatsPanel({ aeronaves }: Props) {
  const decadeCounts = aeronaves.reduce<Record<number, number>>((acc, a) => {
    const d = Math.floor(a.ano / 10) * 10
    acc[d] = (acc[d] ?? 0) + 1
    return acc
  }, {})

  const vendidoCount = aeronaves.filter((a) => a.vendido).length

  const marcaCounts = aeronaves.reduce<Record<string, number>>((acc, a) => {
    acc[a.marca] = (acc[a.marca] ?? 0) + 1
    return acc
  }, {})

  return (
    <>
      <div className="stats-row">
        {Object.entries(decadeCounts)
          .sort((a, b) => Number(a[0]) - Number(b[0]))
          .map(([decade, count]) => (
            <div className="stat-chip" key={decade}>
              <div className="stat-chip__label">Década {String(decade).slice(-2)}</div>
              <div className="stat-chip__value">{count}</div>
              <div className="stat-chip__sub">{count === 1 ? 'aeronave' : 'aeronaves'}</div>
            </div>
          ))}
        <div className="stat-chip">
          <div className="stat-chip__label">Vendidas</div>
          <div className="stat-chip__value">{vendidoCount}</div>
          <div className="stat-chip__sub">{vendidoCount === 1 ? 'aeronave' : 'aeronaves'}</div>
        </div>
        <div className="stat-chip">
          <div className="stat-chip__label">Total</div>
          <div className="stat-chip__value">{aeronaves.length}</div>
          <div className="stat-chip__sub">{aeronaves.length === 1 ? 'aeronave' : 'aeronaves'}</div>
        </div>
      </div>

      {Object.keys(marcaCounts).length > 0 && (
        <div className="marca-list">
          {Object.entries(marcaCounts).map(([marca, count]) => (
            <div className="marca-pill" key={marca}>
              {marca.charAt(0) + marca.slice(1).toLowerCase()}
              <span className="marca-pill__count">{count}</span>
            </div>
          ))}
        </div>
      )}
    </>
  )
}
