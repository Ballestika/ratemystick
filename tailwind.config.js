/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,ts}"],
  safelist: [
    'underline',
    'bg-gray-100'
  ],
  theme: {
    extend: {
      colors: {
        grayCustom: '#3C3C3C', // Color gris
        brownCustom: '#7A3F18', // Color marrón
        whiteCustom: '#F1F1F1', // Color blanco
        yellowCustom: '#FFDA1F', // Color amarillo
      },
    },
    container: {
      center: true,
    }
  },
  plugins: [
    require('@tailwindcss/forms')
  ]
}
