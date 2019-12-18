# cybersecuritymooc-2019-project1

This file is recommended to read in raw mode.

This course project includes a web application that has five different flaws from the OWASP top ten list (https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf).

FLAW 1:
The application is used for taking in registrations for an event. Any user can access to the form page in order to sign up. After sign up the user receives a confirmation. There is also a possibility to view the list of participants, who have already signed up. The list should be accessible only to authorized and authenticated users. Instead the application permits every user to access directly to any page without authentication by modifying the URL. For example, URL “localhost:8080/participants” leads directly to the page, which shows the participant list, without any authentication. The flaw can be categorized as broken access control.

The flaw is fixed by restricting access to any other page than the root and form pages. In practice, an unauthenticated and unauthorized user is only allowed to sign up. In order to fix this flaw, you have to modify http.authorizeRequest() in file SecurityConfiguration.java. Instead of parameter “.anyRequest().permitAll();” use parameters .antMatchers("/").permitAll().antMatchers("/form").permitAll().anyRequest().authenticated();” This still allows any user to access to root and form pages without authentication. The rest of the pages in the application requires the login after the modification.

FLAW 2:
The sole user, who should have access to the participants list is defined in the code as “ted”. There is no method to add or remove users in the application. The username “ted” has default password “ted”, which is stored in an encrypted form. Despite of encryption, this password is obviously weak and easily found out by the attacker. After that, the attacker can take control of the application by changing the password. The flaw can be categorized as security misconfiguration.

The solution to fix this flaw is to chance the default password to the one, that meets the criteria of strong password. You can either chance the default password before starting the application or after you have started the application by using the password changing functionality. The default password is found from the file CustomUserDetailsService.java. 

FLAW 3:
The password changing functionality is accessible through the URL “localhost:8080/participants” which in turn is accessible from the URL “localhost:8080/form” after authenticated login by an authorized user. When user is changing his/her password, the application checks that, inputs of the new password and its confirmation are identical, before the password is changed. However, the old password is not verified during the process. The flaw can be categorized as broken authentication.

The flaw can be fixed by adding a query and verification of the current password of the user, who is changing his/her password. You should add “<p><label for="old">Current password</label>: <input type="password" name="old" id="old"/></p>” to the “password.html” template. Preferably before the similar rows for new password and its confirmation, since it makes it more logical. You also have to modify submitPasswordForm method in file ParticipantsController.java. There is if…else statement in that method which is used for choosing the action after comparing the new password to its confirmation. Adding “&& b.matches(old, user.getPassword())” to the statements condition, makes the method also to check, that the input from the old password query matches to the user’s password stored in the application. 

FLAW 4:
This allows malicious script to be executed when login page redirects user to the participants page. This can be demonstrated by adding new participant to a list from the URL “localhost:8080/form”. Insert for example the following script “<script>alert(“This application is infected!”)</script>” either to the name or the address field and press submit. Check participants by logging in. Instead of a new participant added to a list, alert window pops up. This is possible because of the use of an unescaped text format in “participants.html” template. This text format respects HTML tags in the input and interprets them as html code. The flaw can be categorized as cross-site scripting.

Changing an unescaped text format to an escaped one fixes the flaw. This is done by editing span elements from the “participants.html” template. Change “th:utext” parameter to “th:text”. This parameter does not respect any HTML tags and the script will be displayed as plain text, including tags. This also reveals an attempted cross-site scripting attack.

FLAW 5:
You should always use the most updated versions of components in order to mitigate the risk to be exploited. This is not the case in this application because it uses Spring Boot Starterversion 1.4.2, which is released already on 8 November 2016. There are some known vulnerabilities in that version. Therefore, the flaw can be categorized as using components with known vulnerabilities.

You can update to the version 1.5.9 by only changing the release definition of “spring-boot-starter-parent” in “pom.xml” file. In order to update to the newest version, which is 2.2.2 at the moment (16 Dec 2019), you also have to update Spring Framework to 5.2.2 and Maven to 3.3+, because those are minimum requirements for that release.
