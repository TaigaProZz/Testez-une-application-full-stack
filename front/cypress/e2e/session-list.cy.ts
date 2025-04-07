describe('Session page spec', () => {

  it('try to go on /sessions page being logged out', () => {
    cy.visit('/sessions')
    cy.url().should('include', '/login')
  })

  it('try to go on /sessions page being logged in', () => {
    cy.loginUser()

    cy.url().should('include', '/sessions')
  })

  it('add session being logged in', () => {
    cy.loginUser()

    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        {
          id: 1,
          firstName: "first name",
          lastName: "last name",
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
      ]
    })

    cy.intercept('POST', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "session name",
          description: "a",
          date: new Date(2000, 11, 20),
          teacher_id: 1,
          users: [1, 2],
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
      ]
    }).as('addSession')

    // check redirect to /sessions and click on create button
    cy.url().should('include', '/sessions')
    cy.contains('button', 'Create').click()

    // check title
    cy.get('h1').should('contain.text', 'Create session')

    // check input exists
    cy.get('input[formControlName=name]').should('exist')
    cy.get('input[formControlName=date]').should('exist')
    cy.get('mat-select[formControlName=teacher_id]').should('exist')
    cy.get('textarea[formControlName=description]').should('exist')

    // fill inputs
    cy.get('input[formControlName=name]').type('session name')
    cy.get('input[formControlName=date]').type('2023-01-01')
    cy.get('mat-select[formControlName=teacher_id]').click()
    cy.get('mat-option').contains('first name last name').click()
    cy.get('textarea[formControlName=description]').type('a')

    // check inputs values
    cy.get('input[formControlName=name]').should('have.value', 'session name')
    cy.get('input[formControlName=date]').should('have.value', '2023-01-01')
    cy.get('mat-select[formControlName=teacher_id]')
      .should('contain.text', 'first name last name')
    cy.get('textarea[formControlName=description]').should('have.value', 'a')

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "session name",
          description: "a",
          date: new Date(2000, 11, 20),
          teacher_id: 1,
          users: [1, 2],
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
      ]
    })

    // check submit button is enabled, click it and wait for the response
    cy.get('button[type=submit]').should('not.be.disabled')
    cy.get('button[type=submit]').click()
    cy.wait('@addSession')

    // check redirect to /sessions and success message
    cy.url().should('include', '/sessions')
    cy.contains('Session created !').should('exist')
    cy.contains('session name').should('exist')

    // check if success message disappears after 3 seconds
    cy.wait(3000)
    cy.contains('Session created !').should('not.exist')
  })

  it('update session being logged in', () => {
    cy.loginUser()

    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        {
          id: 1,
          firstName: "first name",
          lastName: "last name",
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
      ]
    })

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "session name",
          description: "a",
          date: new Date(2000, 11, 20),
          teacher_id: 1,
          users: [1, 2],
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
      ]
    })

    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
          id: 1,
          name: "session name",
          description: "a",
          date: new Date(2000, 11, 20),
          teacher_id: 1,
          users: [1, 2],
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
    })

    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "session name",
          description: "a",
          date: new Date(2000, 11, 20),
          teacher_id: 1,
          users: [1, 2],
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
      ]
    }).as('updateSession')

    // check redirect to /sessions and click on update button
    cy.url().should('include', '/sessions')
    cy.contains('button', 'Edit').click()

    // check title
    cy.get('h1').should('contain.text', 'Update session')

    // check input exists
    cy.get('input[formControlName=name]').should('exist')
    cy.get('input[formControlName=date]').should('exist')
    cy.get('mat-select[formControlName=teacher_id]').should('exist')
    cy.get('textarea[formControlName=description]').should('exist')

    // fill inputs
    cy.get('input[formControlName=name]').clear().type('session name')
    cy.get('input[formControlName=date]').clear().type('2023-01-01')
    cy.get('mat-select[formControlName=teacher_id]').click()
    cy.get('mat-option').contains('first name last name').click()
    cy.get('textarea[formControlName=description]').clear().type('a')

    // check inputs values
    cy.get('input[formControlName=name]').should('have.value', 'session name')
    cy.get('input[formControlName=date]').should('have.value', '2023-01-01')
    cy.get('mat-select[formControlName=teacher_id]')
      .should('contain.text', 'first name')
    cy.get('textarea[formControlName=description]').should('have.value', 'a')

    // check submit button is enabled, click it and wait for the response
    cy.get('button[type=submit]').should('not.be.disabled')
    cy.get('button[type=submit]').click()
    cy.wait('@updateSession')

    // check redirect to /sessions and success message
    cy.url().should('include', '/sessions')
    cy.contains('Session updated !').should('exist')
    cy.contains('session name').should('exist')

    // check if success message disappears after 3 seconds
    cy.wait(3000)
    cy.contains('Session updated !').should('not.exist')
  })

  it('delete session being logged in', () => {
    cy.loginUser()

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "session name",
          description: "a",
          date: new Date(2000, 11, 20),
          teacher_id: 1,
          users: [1, 2],
          createdAt: new Date(2000, 11, 20),
          updatedAt: new Date(2000, 11, 20)
        }
      ]
    })


    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: "session name",
        description: "a",
        date: new Date(2000, 11, 20),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(2000, 11, 20),
        updatedAt: new Date(2000, 11, 20)
      }
    })

    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200,
      body: {}
    }).as('deleteSession')

    // check redirect to /sessions and click on delete button
    cy.url().should('include', '/sessions')

    cy.contains('button', 'Detail').click()

    cy.contains('button', 'Delete').click()

    // click on delete button again and confirm
    cy.wait('@deleteSession')

    // check redirect to /sessions and success message
    cy.url().should('include', '/sessions')
    cy.contains('Session deleted !').should('exist')
  })

});
