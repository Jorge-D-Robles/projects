const sampleQuery =
  "My name is Brian. I have 3 semesters left. I am pursuing a BA (Bachelor of Arts) in Computer Science. I have taken the following Computer Science courses: CSCI 111, CSCI 211, CSCI 212, CSCI 220, CSCI 240. I have taken the following Math courses: MATH 120, MATH 141, MATH 142. I have completed 2 Computer Science Electives. I am Comfortable with Math. I have completed 6 general education courses. I would like to prioritize the average GPA of the course given by a specific professor. I consider myself an average student. I can take on 3 CS classes at a time.";

// Example JavaScript code to send data to Flask using the fetch API
document.addEventListener("DOMContentLoaded", function () {
  const headerData = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ message: sampleQuery }),
  };

  fetch("/result", headerData)
    .then((response) => response.json())
    .then((response) => onGptResponse(response))
    .catch((error) => onError(error, sampleQuery));
});

/**
 * In this function one can render what is given after the response.
 * @param {*} response The markdown of what gpt responded to
 */
function onGptResponse(response) {
  console.log("Recieved response form gpt");
  console.log(response);
}

/**
 * Consider redirecting a user or telling to restart the application
 * @param {*} error The specific error that was caused during this transaction
 * @param {*} userInput The prompt that was given to gpt
 */
function onError(error, userInput) {
  console.error("Error sending data:", error);
  console.log("Using query/data", userInput);
  //   Redirect user to try again
}
