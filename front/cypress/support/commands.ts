// cypress/support/commands.js

// create login command

Cypress.Commands.add('loginUser', () => {
  cy.visit('/login');

  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    },
  })

  cy.intercept(
    {
      method: 'GET',
      url: '/api/session',
    },
    [])

  cy.intercept('GET', '/api/user/1', {
    body: {
      email: 'a@a.a',
      firstName: 'a',
      lastName: 'a',
      admin: false,
      createdAt: new Date(2000, 11, 20),
      updatedAt: new Date(2000, 11, 20)
    }
  })

  cy.get('input[formControlName=email]').type("a@a.a");
  cy.get('input[formControlName=password]').type("a");
  cy.get('button[type=submit]').click();
});



Cypress.Commands.add('loginAdmin', () => {
  cy.visit('/login');

  cy.intercept('POST', '/api/auth/login', {
    statusCode: 200,
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    },
  })

  cy.intercept(
    {
      method: 'GET',
      url: '/api/session',
    },
    [])

  cy.intercept('GET', '/api/user/1', {
    body: {
      email: 'a@a.a',
      firstName: 'a',
      lastName: 'a',
      admin: true,
      createdAt: new Date(2000, 11, 20),
      updatedAt: new Date(2000, 11, 20)
    }
  })

  cy.get('input[formControlName=email]').type("a@a.a");
  cy.get('input[formControlName=password]').type("a");
  cy.get('button[type=submit]').click();
});
