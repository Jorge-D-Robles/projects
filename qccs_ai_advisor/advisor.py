from flask import Flask
from flask import render_template, request, jsonify

from dotenv import dotenv_values
import os
from time import sleep
import openai
from openai import OpenAI

from utilities import checkOpenAIVersionCompatibaility, system_message

checkOpenAIVersionCompatibaility()

secrets = dotenv_values(".env")
OPENAI_API_KEY = secrets['OPENAI_API_KEY']
csv_file_path = "./Grade_Distribution_for_HackCUNY.csv"
client = OpenAI(api_key=OPENAI_API_KEY)


app = Flask(__name__)


@app.route('/')
def index():
    """
    Display the index page accessible at the root URL.
    """
    return render_template("src/index.html")

@app.route('/about')
def about():
    """
    Display the about page accessible at '/about' URL.
    """
    return render_template("src/about.html")

@app.route('/result', methods=['POST'])
def result():
    import csv
    """
    Calling this endpoint assumes you are giving a valid message. A response is the query result that
    was trained on a dataset on Queens College Computer Science grade distribution.
    """

    #  Initialize an empty list to collect information from each row
    all_teacher_class_info = []

    i = 0
    with open(csv_file_path, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            # Creating a structured sentence for each row. Adjust the keys based on your CSV's column names.
            structured_info = f"Professor {row['PROF']} taught {row['COURSE NAME']} with an average GPA of {row['AVG GPA']}, {row['W']} withdrawals out of {row['TOTAL']} students."
            if i < 10:
                print(structured_info)
            all_teacher_class_info.append(structured_info)
            i += 1

    # Combine all structured information, separating each entry with a newline
    aggregated_info = '\n'.join(all_teacher_class_info)

    query = request.json
    user_input = query.get("message", ' ')
    print("Got request for prompt", user_input)

    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": system_message},
            {"role": "user", "content": user_input},
            {'role': "system", "content": aggregated_info}
        ],
    )

    schedule = response.choices[0].message.content
    print("GPT response", schedule)
    return jsonify({'assistant_response': schedule})
