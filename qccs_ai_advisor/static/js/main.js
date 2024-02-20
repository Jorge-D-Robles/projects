/* START OF MODAL SECTION */

const modalOpen = document.querySelector(".modal-open"); // The element that opens the modal
const modalCloseButtons = document.querySelectorAll(".modal-close"); // Selects all close buttons, including the "X"
const modal = document.getElementById("modal"); // The modal itself
const modalOverlay = document.getElementById("modalOverlay"); // The overlay

// Function to open the modal
const openModal = () => {
  modal.classList.remove("hidden");
  modalOverlay.classList.remove("hidden");
};

// Function to close the modal
const closeModal = () => {
  modal.classList.add("hidden");
  modalOverlay.classList.add("hidden");
};

// Open modal event
if (modalOpen) {
  modalOpen.addEventListener("click", openModal);
}

// Close modal when clicking on the overlay
modalOverlay.addEventListener("click", (event) => {
  if (event.target === modalOverlay) {
    closeModal();
  }
});

// Close modal when clicking the "X" or any .modal-close element
modalCloseButtons.forEach((closeButton) => {
  closeButton.addEventListener("click", closeModal);
});

// Close modal with the ESC key
document.addEventListener("keydown", (event) => {
  if (event.key === "Escape" && !modal.classList.contains("hidden")) {
    closeModal();
  }
});
/* END OF MODAL SECTION */

/*

STATE MACHINE FOR PROMPTS

*/

let currentState = 0;
let currentAnswer = "";
const states = [
  {
    title: "Welcome!",
    paragraph: "What is your name?",
    inputType: "text",
    placeholder: "Type your answer...",
    onNext: () => {
      const input = hero.querySelector('input[type="text"]');
      if (input) {
        promptText = `My name is ${input.value}. `;
      }
      transitionToState(1, promptText);
    },
  },
  {
    title: "Figuring out your needs...",
    paragraph: "How many semesters do you have left?",
    inputType: "dropdown",
    options: [
      { label: "1", value: "1" },
      { label: "2", value: "2" },
      { label: "3", value: "3" },
      { label: "4", value: "4" },
      { label: "5", value: "5" },
      { label: "6", value: "6" },
      { label: "7", value: "7" },
      { label: "8", value: "8" },
    ],
    onNext: () => {
      const dropdownButton = hero.querySelector("button");
      if (dropdownButton) {
        promptText = `I have ${dropdownButton.textContent.trim()} semesters left. `;
      }
      transitionToState(2, promptText);
    },
  },
  {
    title: "Figuring out your needs...",
    paragraph:
      "Are you pursuing a BA (Bachelor of Arts) or BS (Bachelor of Science)?",
    inputType: "radio",
    options: [
      {
        label: "BA (Bachelor of Arts) in Computer Science",
        value: "BA (Bachelor of Arts) in Computer Science",
      },
      {
        label: "BS (Bachelor of Science) in Computer Science",
        value: "BS (Bachelor of Science) in Computer Science",
      },
    ],
    onNext: () => {
      const chosenOption = hero.querySelector('input[type="radio"]:checked');
      promptText = `I am pursuing a ${chosenOption.labels[0].textContent}. `;
      transitionToState(3, promptText);
    },
  },
  {
    title: "CS Courses taken...",
    paragraph:
      "Select the REQUIRED/NON ELECTIVE Computer Science courses you have taken so far.",
    inputType: "checkbox",
    options: [
      { label: "No classes taken", value: "No classes taken" },
      { label: "CSCI 111", value: "CSCI 111" },
      { label: "CSCI 211", value: "CSCI 211" },
      { label: "CSCI 212", value: "CSCI 212" },
      { label: "CSCI 220", value: "CSCI 220" },
      { label: "CSCI 240", value: "CSCI 240" },
      { label: "CSCI 313", value: "CSCI 313" },
      { label: "CSCI 316", value: "CSCI 316" },
      { label: "CSCI 320", value: "CSCI 320" },
      { label: "CSCI 323", value: "CSCI 323" },
      { label: "CSCI 331", value: "CSCI 331" },
      { label: "CSCI 340", value: "CSCI 340" },
      { label: "CSCI 343", value: "CSCI 343" },
      { label: "CSCI 370", value: "CSCI 370" },
    ],
    onNext: () => {
      promptText = `I have taken the following Computer Science courses: `;
      const checkboxes = hero.querySelectorAll('input[type="checkbox"]');
      checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
          promptText += `${checkbox.labels[0].textContent}, `;
        }
      });
      // remove the last "," and replace with a "."
      promptText = promptText.slice(0, -2) + ". ";

      transitionToState(4, promptText);
    },
  },
  {
    title: "Math Courses taken...",
    paragraph:
      "Select the REQUIRED/NON ELECTIVE Math courses you have taken so far. Note: You cannot take MATH 141-142-143 if you have taken MATH 151-152 and vice-versa.",
    inputType: "checkbox",
    options: [
      { label: "No classes taken", value: "No classes taken" },
      { label: "MATH 120", value: "MATH 120" },
      { label: "MATH 141", value: "MATH 141" },
      { label: "MATH 142", value: "MATH 142" },
      { label: "MATH 143", value: "MATH 143" },
      { label: "MATH 151", value: "MATH 151" },
      { label: "MATH 152", value: "MATH 152" },
      { label: "MATH 231", value: "MATH 231" },
      { label: "MATH 241", value: "MATH 241" },
    ],
    onNext: () => {
      promptText = `I have taken the following Math courses: `;
      const checkboxes = hero.querySelectorAll('input[type="checkbox"]');
      checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
          promptText += `${checkbox.labels[0].textContent}, `;
        }
      });
      // remove the last "," and replace with a "."
      promptText = promptText.slice(0, -2) + ". ";

      transitionToState(5, promptText);
    },
  },
  {
    title: "Computer Science electives taken...",
    paragraph:
      "How many Computer Science Electives have you completed? (Note: You need 3 or 7 to graduate for BA or BS, respectively)",
    inputType: "dropdown",
    options: [
      { label: "0", value: "0" },
      { label: "1", value: "1" },
      { label: "2", value: "2" },
      { label: "3", value: "3" },
      { label: "4", value: "4" },
      { label: "5", value: "5" },
      { label: "6", value: "6" },
      { label: "7", value: "7" },
    ],
    onNext: () => {
      const dropdownButton = hero.querySelector("button");
      if (dropdownButton) {
        promptText = `I have completed ${dropdownButton.textContent.trim()} Computer Science Electives. `;
      }
      transitionToState(6, promptText);
    },
  },
  {
    title: "Math Rigour...",
    paragraph:
      "What is your comfort level with Math? Choose either comfortable or uncomfortable. Note: We will recommend different Calculus courses based on your answer.",
    inputType: "radio",
    options: [
      { label: "Comfortable", value: "Comfortable" },
      { label: "Uncomfortable", value: "Uncomfortable" },
    ],
    onNext: () => {
      const chosenOption = hero.querySelector('input[type="radio"]:checked');
      promptText = `I am ${chosenOption.labels[0].textContent} with Math. `;
      transitionToState(7, promptText);
    },
  },
  {
    title: "General Education Requirements...",
    paragraph:
      "How many general education courses have you completed? You need around 16 to graduate.",
    inputType: "dropdown",
    options: [
      { label: "0", value: "0" },
      { label: "1", value: "1" },
      { label: "2", value: "2" },
      { label: "3", value: "3" },
      { label: "4", value: "4" },
      { label: "5", value: "5" },
      { label: "6", value: "6" },
      { label: "7", value: "7" },
      { label: "8", value: "8" },
      { label: "9", value: "9" },
      { label: "10", value: "10" },
      { label: "11", value: "11" },
      { label: "12", value: "12" },
      { label: "13", value: "13" },
      { label: "14", value: "14" },
      { label: "15", value: "15" },
      { label: "16", value: "16" },
    ],
    onNext: () => {
      const dropdownButton = hero.querySelector("button");
      if (dropdownButton) {
        promptText = `I have completed ${dropdownButton.textContent.trim()} general education courses. `;
      }
      transitionToState(8, promptText);
    },
  },
  {
    title: "GPA...",
    paragraph:
      "When being recommend courses, would you like to prioritize the average GPA of the course given by a specific professor?",
    inputType: "radio",
    options: [
      { label: "Yes", value: "Yes" },
      { label: "No", value: "No" },
    ],
    onNext: () => {
      const chosenOption = hero.querySelector('input[type="radio"]:checked');
      if (chosenOption.labels[0].textContent === "No") {
        promptText = `I would not like to prioritize the average GPA of the course given by a specific professor. `;
        transitionToState(9, promptText);
      } else {
        promptText = `I would like to prioritize the average GPA of the course given by a specific professor. `;
        transitionToState(9, promptText);
      }
    },
  },
  {
    title: "How you feel about the course load...",
    paragraph:
      "Do you consider yourself to be currently struggling in school? Note: We will recommend different courses based on your answer.",
    inputType: "radio",
    options: [
      {
        label:
          "I struggle heavily with school. Please suggest only 1-2 CS classes at a time",
        value:
          "I struggle heavily with school. Please suggest only 1-2 CS classes at a time",
      },
      {
        label:
          "I struggle a bit with school. I can take on 2 CS classes at a time.",
        value:
          "I struggle a bit with school. I can take on 2 CS classes at a time",
      },
      {
        label:
          "I consider myself an average student. I can take on 3 CS classes at a time",
        value:
          "I consider myself an average student. I can take on 3 CS classes at a time",
      },
      {
        label:
          "I am willing to take on up to 4-5 CS classes and do not struggle in my CS courses",
        value:
          "I am willing to take on up to 4-5 CS classes and do not struggle in my CS courses",
      },
    ],
    onNext: () => {
      const chosenOption = hero.querySelector('input[type="radio"]:checked');

      promptText = `${chosenOption.labels[0].textContent}. Please suggest a semester by semester plan for the remaining semesters I have left, giving me the most optimal courses to take while taking prerequisites into account. Please respond using markdown syntax, with your first line of your response being "#Your Personalized Course Plan" and the rest of your response being your plan. When displaying the course plan semester by semester, please format it into a table. Generate the table once, with professor suggestions and everything else. Generate one table per semester, with a title for each table being the semester number. Suggest one or more professors for each class if they have grade data in the grade distribution database provided. Do not suggest "Best Available" for professors if they have grade data in the grade distribution database provided. Use the prior historical data to suggest professors. If you have to choose one professor per class, choose the one with the highest average GPA. The second or third professors recommended could be in descending order of average GPA. Give general computer science advice tailored to my needs. If I am struggling with Math, suggest Khan Academy or Professor Leonard on Youtube as resources to improve. If I am struggling with computer science, suggest going to the learning commons at Kiely Hall for free tutoring or find resources such as Harvard's CS50x or MIT OpenCourseWare to supplement my learning. These suggestions should generally be given regardless. Write a full paragraph of general advice for computer science students with these ideas and suggestions in mind, using a approachable but professional tone for each semester.`;
      transitionToState(10, promptText);
    },
    initialButtonState: true, // Enable the button by default
  },
  {
    title: "Almost Ready!",
    paragraph:
      "We have all the information we need. Click the button below to receive your personalized course plan.",
    inputType: "button",
    onNext: () => {
      const button = hero.querySelector("button");
      const headerData = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ message: currentAnswer }),
      };

      if (button) {
        button.textContent = "Loading...";
        button.disabled = true;

        // Display a loading message and a loader in the hero section
        hero.innerHTML = `
          <div class="text-center">
            <p class="text-lg mb-6">Our AI is generating custom advice just for you! Please wait...</p>
            <div class="flex justify-center items-center">
              <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-red-500"></div>
            </div>
          </div>
        `;

        // Fetch the API response
        fetch("/result", headerData)
          .then((response) => response.json())
          .then((response) => {
            const text = response["assistant_response"];
            // Display the API response using marked for markdown parsing
            hero.innerHTML = `<article class="markdown prose prose-sm sm:prose lg:prose-lg xl:prose-xl">${marked.parse(
              text
            )}</article>`;
            // Create a button element
            const button = document.createElement("button");
            // Add the desired classes to the button
            button.classList.add(...defaultButtonClasses);
            // Set the button text
            button.textContent = "Back To Home";
            // Add an event listener to the button that changes the window location to '/' when the button is clicked
            button.addEventListener("click", () => {
              window.location.href = "/";
            });
            // Append the button to the hero element
            hero.appendChild(button);
          })
          .catch((error) => {
            console.error(error);
            // display an error message
            hero.innerHTML =
              "<p>Error loading data. Please try again later.</p>";
          })
          .finally(() => {
            // Re-enable the button and potentially remove the loader after the operation is complete
            button.textContent = "Done!";
            button.disabled = false;
          });
      }
    },
  },
];

function transitionToState(index, promptText = "") {
  currentAnswer += promptText;
  console.log(currentAnswer);
  currentState = index;
  const state = states[index];
  updateHeroContent({
    titleText: state.title,
    paragraphText: state.paragraph,
    inputType: state.inputType,
    inputPlaceholder: state.placeholder,
    options: state.options, // Make sure to pass options here
    buttonText: "Next",
    buttonId: `nextButton${index + 1}`,
    onNext: state.onNext,
  });
}

/* START OF GET STARTED SECTION */

// Click event for the "Get Started" button
const startButton = document.querySelector("#getStarted");
const hero = document.querySelector("#hero");
hero.classList.add("transition-opacity", "duration-1000");

startButton.addEventListener("click", () => {
  // Fade out the hero section smoothly
  hero.classList.add("opacity-0");

  // Wait for the fade-out to complete
  setTimeout(() => {
    // Update the hero section content
    const readMore = document.querySelector("#readMore");
    const title = document.querySelector("#title");
    const innerPara = document.querySelector("#innerPara");
    const modalTrigger = document.querySelector("#modal-trigger");

    if (title) title.textContent = "Let's get started.";
    if (innerPara)
      innerPara.textContent =
        "You will be answering a set of questions to help us understand your needs. Choose each answer based off your personal experience and preferences in order to receive a personalized recommendation.";
    if (readMore) readMore.remove();
    if (modalTrigger) modalTrigger.remove();
    if (startButton) startButton.remove();
    // Create a wrapper div for the button
    const buttonWrapper = document.createElement("div");
    buttonWrapper.classList.add("flex", "justify-center", "w-full");

    // Prepare the button with your existing setup
    const startSurvey = document.createElement("button");
    startSurvey.textContent = "Click here to begin";
    startSurvey.classList.add(
      "mt-6",
      "inline-flex",
      "items-center",
      "px-6",
      "py-3",
      "border",
      "border-transparent",
      "text-base",
      "font-medium",
      "rounded-md",
      "text-white",
      "bg-red-600",
      "hover:bg-red-700"
    );
    startSurvey.id = "startSurvey";
    // Initial trigger
    startSurvey.addEventListener("click", () => transitionToState(0));

    // Append the button to the wrapper, then the wrapper to the hero section
    buttonWrapper.appendChild(startSurvey);
    hero.appendChild(buttonWrapper);

    // Prepare for fade-in by making the hero invisible and then triggering reflow
    hero.classList.add("opacity-0");
    void hero.offsetWidth; // Trigger reflow to ensure the next opacity change is animated

    // Fade in the hero section smoothly with the new content
    setTimeout(() => {
      hero.classList.remove("opacity-0");
    }, 10); // A short delay to ensure the opacity change triggers a transition
  }, 1000); // This timeout should match the fade-out transition duration
});

/*
.
.
UPDATING HERO CONTENT
.
*/

// Define default button classes if not provided
const defaultButtonClasses = [
  "mt-6",
  "inline-flex",
  "items-center",
  "px-6",
  "py-3",
  "border",
  "border-transparent",
  "text-base",
  "font-medium",
  "rounded-md",
  "text-white",
  "bg-red-600",
  "hover:bg-red-700",
];
const buttonClasses = defaultButtonClasses; // default set of button classes

// Define the updateHeroContent function with an options object parameter that includes all possible options.
function updateHeroContent({
  titleText,
  paragraphText,
  inputType,
  inputPlaceholder,
  options,
  buttonText,
  buttonId,
  onNext,
  initialButtonState = false, // By default, buttons are disabled until an action makes them active
}) {
  // Start fade-out animation
  hero.classList.add("opacity-0");
  setTimeout(() => {
    // Clear existing content and set up the hero section after fade-out
    hero.innerHTML = "";
    hero.classList.add(
      "flex",
      "flex-col",
      "items-center",
      "justify-center",
      "h-full"
    );

    // Append title
    if (titleText) {
      const title = document.createElement("h2");
      title.textContent = titleText;
      title.classList.add("text-xl", "font-bold");
      hero.appendChild(title);
    }

    // Append paragraph
    if (paragraphText) {
      const paragraph = document.createElement("p");
      paragraph.textContent = paragraphText;
      paragraph.classList.add("mt-4");
      hero.appendChild(paragraph);
    }
    // Create the action button
    const actionButton = document.createElement("button");
    actionButton.textContent = buttonText;
    actionButton.id = buttonId;
    actionButton.classList.add(
      "mt-4",
      "px-6",
      "py-5",
      "bg-red-600",
      "text-white",
      "font-medium",
      "text-sm",
      "rounded-md",
      "shadow-sm",
      "hover:bg-red-700",
      "focus:outline-none",
      "focus:ring-2",
      "focus:ring-indigo-500",
      "focus:ring-offset-2",
      "disabled:opacity-50"
    );
    actionButton.disabled = !initialButtonState; // Set based on initialButtonState parameter

    // Handle input creation based on type
    if (inputType === "text") {
      const input = document.createElement("input");
      input.type = "text";
      input.placeholder = inputPlaceholder || "";
      input.classList.add(
        "mt-4",
        "p-2",
        "border",
        "border-gray-300",
        "rounded-md",
        "w-full"
      );
      hero.appendChild(input);

      // Event listener for enabling the action button when text is entered
      input.addEventListener("input", () => {
        actionButton.disabled = input.value.trim() === "";
      });
    } // Updated section for handling radio buttons and checkboxes
    else if (inputType === "dropdown" && options) {
      // Create the dropdown container
      const dropdownContainer = document.createElement("div");
      dropdownContainer.classList.add("relative", "inline-block", "text-left");

      // Dropdown button
      const dropdownButton = document.createElement("button");
      dropdownButton.setAttribute("type", "button");
      dropdownButton.classList.add(
        "inline-flex",
        "justify-center",
        "rounded-md",
        "bg-white",
        "px-4",
        "py-2",
        "text-sm",
        "font-medium",
        "text-gray-700",
        "shadow-sm",
        "ring-1",
        "ring-gray-300",
        "hover:bg-gray-50",
        "w-48"
      ); // Adjust width as needed
      let buttonText = "Options"; // Initial button text
      dropdownButton.innerHTML = `${buttonText} <svg class="-mr-1 ml-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z" clip-rule="evenodd" /></svg>`;

      dropdownContainer.appendChild(dropdownButton);

      // Dropdown menu
      const dropdownMenu = document.createElement("div");
      dropdownMenu.classList.add(
        "hidden",
        "absolute",
        "z-10",
        "mt-2",
        "w-48",
        "origin-top-right",
        "rounded-md",
        "bg-white",
        "shadow-lg",
        "ring-1",
        "ring-black",
        "ring-opacity-5",
        "focus:outline-none"
      ); // Adjust width to match button
      dropdownMenu.setAttribute("role", "menu");
      dropdownMenu.setAttribute("aria-orientation", "vertical");
      dropdownMenu.setAttribute("aria-labelledby", "menu-button");

      options.forEach((option, index) => {
        const optionLink = document.createElement("a");
        optionLink.href = "#";
        optionLink.classList.add(
          "text-gray-700",
          "block",
          "px-4",
          "py-2",
          "text-sm",
          "hover:bg-gray-100",
          "hover:text-gray-900"
        );
        optionLink.setAttribute("role", "menuitem");
        optionLink.textContent = option.label;
        optionLink.addEventListener("click", (e) => {
          e.preventDefault();
          dropdownMenu.classList.add("hidden"); // Hide dropdown menu on selection
          dropdownButton.innerHTML = `${option.label} <svg class="-mr-1 ml-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true"><path fill-rule="evenodd" d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z" clip-rule="evenodd" /></svg>`; // Update button text to selected option
          actionButton.disabled = false; // Optionally, enable the action button
        });
        dropdownMenu.appendChild(optionLink);
      });

      dropdownContainer.appendChild(dropdownMenu);
      hero.appendChild(dropdownContainer);

      // Event listener for opening/closing the dropdown
      dropdownButton.addEventListener("click", () => {
        dropdownMenu.classList.toggle("hidden");
      });
    } else if (inputType === "radio" && options) {
      // Radio buttons
      const fieldset = document.createElement("fieldset");
      const legend = document.createElement("legend");

      fieldset.appendChild(legend);

      options.forEach((option, index) => {
        const radioContainer = document.createElement("div");
        radioContainer.classList.add("flex", "items-center", "mt-4", "mb-4");

        const input = document.createElement("input");
        input.id = `${buttonId}-${index}`;
        input.name = buttonId; // Ensure all radios have the same name
        input.type = "radio";
        input.classList.add(
          "h-4",
          "w-4",
          "text-red-600",
          "focus:ring-red-500",
          "border-gray-300"
        );

        const label = document.createElement("label");
        label.htmlFor = `${buttonId}-${index}`;
        label.textContent = option.label;
        label.classList.add("ml-4", "block", "text-base");

        radioContainer.appendChild(input);
        radioContainer.appendChild(label);
        fieldset.appendChild(radioContainer);
      });

      hero.appendChild(fieldset);

      // Setting the event listener for enabling the Next button
      fieldset.addEventListener("change", () => {
        actionButton.disabled = !fieldset.querySelector(
          'input[type="radio"]:checked'
        );
      });
    } else if (inputType === "button") {
      // don't add anything, just make action button instantly available
      actionButton.disabled = false;
    } else if (inputType === "checkbox" && options) {
      // Checkboxes
      const div = document.createElement("div");
      div.classList.add("space-y-4");

      options.forEach((option, index) => {
        const checkboxContainer = document.createElement("div");
        checkboxContainer.classList.add("flex", "items-start", "mt-4", "mb-4");

        const input = document.createElement("input");
        input.id = `${buttonId}-${index}`;
        input.name = option.label; // Unique name for each checkbox
        input.type = "checkbox";
        input.classList.add(
          "h-4",
          "w-4",
          "text-red-600",
          "focus:ring-red-500",
          "border-gray-300",
          "rounded"
        );

        const label = document.createElement("label");
        label.htmlFor = `${buttonId}-${index}`;
        label.textContent = option.label;
        label.classList.add("ml-4", "text-base");

        checkboxContainer.appendChild(input);
        checkboxContainer.appendChild(label);
        div.appendChild(checkboxContainer);
      });

      hero.appendChild(div);

      // Setting the event listener for enabling the Next button
      div.addEventListener("change", () => {
        const checkboxes = div.querySelectorAll('input[type="checkbox"]');
        actionButton.disabled = ![...checkboxes].some(
          (checkbox) => checkbox.checked
        );
      });
    }

    hero.appendChild(actionButton);
    // Event listener for the action button
    actionButton.addEventListener("click", () => {
      if (typeof onNext === "function") {
        onNext();
      }
    });

    // Fade-in the hero section smoothly with the new content
    setTimeout(() => {
      hero.classList.remove("opacity-0");
    }, 10); // A short delay to ensure the opacity change triggers a transition
  }, 300); // Delay for fade-out to complete
}
