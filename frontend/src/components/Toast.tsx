interface ToastProps {
  message: string
  type: 'ok' | 'err'
}

export function Toast({ message, type }: ToastProps) {
  return <div className={`toast toast--${type}`}>{message}</div>
}
