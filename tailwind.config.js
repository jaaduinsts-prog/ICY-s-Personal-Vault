/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        carbon: {
          950: '#050811',
          900: '#070B11',
          800: '#0D1422',
          700: '#141F33',
          600: '#1E314F',
        },
        arc: {
          primary: '#00E5FF',
          glow: '#64FFDA',
          dark: '#007799',
          dim: '#00384D',
        },
        stark: {
          gold: '#FFD700',
          amber: '#FFB703',
          orange: '#FB8500',
        },
        telemetry: {
          green: '#00F5D4',
          warning: '#FF5252',
          purple: '#B388FF',
        }
      },
      fontFamily: {
        mono: ['Courier New', 'Consolas', 'monospace'],
      }
    },
  },
  plugins: [],
}
