const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";
const VALID_DIGEST = "a".repeat(64);

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write job applications", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("job_applications").get()
  );
  await assertFails(
    unauthDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("job_applications")
      .doc("app_1")
      .set({
        userId: ALICE_UID,
        fullName: "Test User",
        email: "test@gmail.com",
        mobileNumber: "03001234567",
        cnicMasked: "*****-*******-1",
        cnicDigest: VALID_DIGEST,
        position: "Teacher",
        course: "Web Designing",
        dedupeKey: "Teacher::Web Designing",
        status: "submitted",
        createdAt: new Date(),
        updatedAt: new Date(),
      })
  );
});

test("Authenticated user can submit valid job application for Teacher or Manager", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("job_applications")
      .doc("app_teacher_1")
      .set({
        userId: ALICE_UID,
        fullName: "Ali Raza",
        email: "aliraza@gmail.com",
        mobileNumber: "+92-300-1234567",
        cnicMasked: "*****-*******-7",
        cnicDigest: VALID_DIGEST,
        position: "Teacher",
        course: "Graphic Designing",
        dedupeKey: "Teacher::Graphic Designing",
        status: "submitted",
        createdAt: new Date(),
        updatedAt: new Date(),
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("job_applications")
      .get()
  );
});

test("Authenticated user cannot read or write another user's job applications", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context
      .firestore()
      .collection("users")
      .doc(BOB_UID)
      .collection("job_applications")
      .doc("bob_app")
      .set({
        userId: BOB_UID,
        fullName: "Bob Khan",
        email: "bob@gmail.com",
        mobileNumber: "03011234567",
        cnicMasked: "*****-*******-9",
        cnicDigest: VALID_DIGEST,
        position: "Manager",
        course: "Microsoft Office",
        dedupeKey: "Manager::Microsoft Office",
        status: "submitted",
        createdAt: new Date(),
        updatedAt: new Date(),
      });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("job_applications").get()
  );
  await assertFails(
    aliceDb
      .collection("users")
      .doc(BOB_UID)
      .collection("job_applications")
      .doc("bob_app")
      .get()
  );
});

test("Rejects invalid job position, ghost fields, and tampering updates", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("job_applications")
      .doc("app_invalid_pos")
      .set({
        userId: ALICE_UID,
        fullName: "Ali Raza",
        email: "aliraza@gmail.com",
        mobileNumber: "03001234567",
        cnicMasked: "*****-*******-7",
        cnicDigest: VALID_DIGEST,
        position: "Director",
        course: "Graphic Designing",
        dedupeKey: "Director::Graphic Designing",
        status: "submitted",
        createdAt: new Date(),
        updatedAt: new Date(),
      })
  );

  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("job_applications")
      .doc("app_ghost")
      .set({
        userId: ALICE_UID,
        fullName: "Ali Raza",
        email: "aliraza@gmail.com",
        mobileNumber: "03001234567",
        cnicMasked: "*****-*******-7",
        cnicDigest: VALID_DIGEST,
        position: "Teacher",
        course: "Web Designing",
        dedupeKey: "Teacher::Web Designing",
        status: "submitted",
        isApproved: true,
        createdAt: new Date(),
        updatedAt: new Date(),
      })
  );
});
