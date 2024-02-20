# QCCS AI Advisor Documentation

## Overview

The QCCS AI Advisor is a Flask-based web application designed to assist Queens College Computer Science students in navigating their academic journey. Utilizing OpenAI's GPT model, the application offers personalized academic advice, including course selections and professor recommendations, based on the student's input and historical grade distribution data. This application was developed as part of the HackCUNY 2024 Hackathon by Jorge Robles, Brian Flores, Thomas Soupionis, and Samiha Zaman.

## Features

- **Modal Interactions**: Utilizes modals for enhanced user interaction.
- **State Machine for Dynamic Prompts**: Guides the user through a series of questions to gather necessary academic information.
- **AI-Powered Recommendations**: Leverages OpenAI's GPT model to generate customized academic advice.
- **Integration with External Data**: Incorporates Queens College Computer Science grade distribution data for informed recommendations.
- **Responsive Design**: Ensures a seamless experience across various devices and screen sizes.

## Technologies Used

- **Frontend**: HTML, Tailwind CSS, JavaScript
- **Backend**: Flask (Python)
- **AI Model**: OpenAI GPT 3.5-Turbo
- **Data Source**: CSV file containing grade distribution data

## Setup and Installation

1. **Clone the Repository**

```bash
git clone <repository-url>
cd <project-directory>
```

2. **Environment Setup**

Create a `.env` file in the project root and add your OpenAI API key:

```
OPENAI_API_KEY=<your_openai_api_key>
```

3. **Install Dependencies**

Ensure you have Python installed and then run:

```bash
pip3 install flask python-dotenv openai
```

Install Tailwind CSS via npm:

```bash
npm install -D tailwindcss@latest postcss@latest autoprefixer@latest
```
1. **Run the Application**

```bash
flask --app advisor run
```

Navigate to `http://127.0.0.1:5000/` to view the application.

## Application Flow

1. **User Interaction**: Users are greeted with a series of questions about their academic progress and preferences.
2. **Data Collection**: User responses are collected and formatted as prompts for the OpenAI GPT model.
3. **AI Processing**: The backend sends the collected data to OpenAI's API, along with historical grade distribution information, to generate personalized advice.
4. **Display Recommendations**: The frontend displays the AI-generated academic advice in a user-friendly format.

## Customizing the AI Model

To adjust the AI model's behavior or incorporate additional datasets, modify the `system_message` and processing logic within `app.route('/result', methods=['POST'])` in the Flask app.

## Security Considerations

While the application prioritizes data security, ensuring the safe handling of student information, it's recommended to review Flask and OpenAI documentation for best practices on securing API keys and user data.

## Future Enhancements

- **Enhanced Data Integration**: Further integration with external academic resources such as CUNYFirst for real-time data.
- **Mobile Application**: Development of a mobile-friendly version of the application for on-the-go access.
- **Enhanced AI Model**: Utilization of more advanced AI models for improved recommendations.
- **Interactive Visualizations**: Incorporation of interactive charts and graphs for better data visualization.
- **Accessibility Features**: Implementation of accessibility features for users with disabilities.
- **Rebuild with React**: Rebuilding the application with React for improved performance and scalability.
- **Automated Testing**: Implementation of automated testing for improved code quality and reliability.
- **Deployment**: Deployment of the application to a cloud platform for public access.
- **Add Support for Multiple Departments**: Expanding the application to support multiple departments within Queens College.

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests with any new features or fixes.

---

This documentation provides a comprehensive overview of the QCCS AI Advisor project. For more detailed information on specific components or further assistance, please refer to the respective documentation of the technologies used.
