# Code Quality 
This Plugin adds and configures some code quality external plugins to analyse code and do some code quality improvements.

---

### Code Formatting | [Spotless Plugin](https://github.com/diffplug/spotless)
The Grade [Spotless plugin](https://github.com/diffplug/spotless) is enabled and added into a git pre-commit hook (see: `.git/hooks` in your project directory), to make sure the code is formatted using the [Palantir style](https://github.com/palantir/palantir-java-format).

---

### Analysis - Common programming mistakes | [Error Prone](https://github.com/google/error-prone) 
The Gradle Plugin Error Prone is added for analysing common mistakes during build time. This should help developers to catch mistakes early in the dev phrase.

---

### Analysis - Null pointer Exceptions | [NullAway](https://github.com/uber/NullAway)
The Gradle Plugin NullAway is added for analyse `null` values by Annotations to eliminate NullPointerExceptions. More infos in the [official repo](https://github.com/uber/NullAway)