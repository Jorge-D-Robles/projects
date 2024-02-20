const defaultTheme = require("tailwindcss/defaultTheme");
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./static/**/*.js", "./templates/**/*.html"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Inter var", ...defaultTheme.fontFamily.sans],
      },
      typography: (theme) => ({
        DEFAULT: {
          css: {
            // Customizations specific to markdown rendered content
            color: theme('colors.gray.700'),
            'h1, h2, h3, h4': {
              color: theme('colors.gray.900'),
            },
            a: {
              color: theme('colors.blue.600'),
              '&:hover': {
                color: theme('colors.blue.700'),
              },
            },
            // Add more custom styles as needed
          },
        },
      }),
    },
  },
  plugins: [
    require("@tailwindcss/forms"),
    require("@tailwindcss/typography"),
  ],
};


// defaultTheme = require("tailwindcss/defaultTheme");
// /** @type {import('tailwindcss').Config} */
// module.exports = {
//   content: ["./static/**/*.js", "./templates/**/*.html"],
//   theme: {
//     extend: {
//       fontFamily: {
//         sans: ["Inter var", ...defaultTheme.fontFamily.sans],
//       },
//     },
//   },
//   plugins: [require("@tailwindcss/forms"), require("@tailwindcss/typography")],
// };
