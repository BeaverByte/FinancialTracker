import { createFileRoute } from "@tanstack/react-router";
import { CardWithForm } from "../components/Card";

export const Route = createFileRoute("/test")({
  component: CardWithForm,
});
