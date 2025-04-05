declare namespace Cypress {
  interface Chainable {
    loginUser(): Chainable<Element>;
    loginAdmin(): Chainable<Element>
  }
}
