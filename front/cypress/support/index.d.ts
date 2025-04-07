declare namespace Cypress {
  interface Chainable {
    loginUser(): Chainable<Element>;
    loginAdmin(): Chainable<Element>;
    loginUserWithSessions(): Chainable<Element>;
  }
}
