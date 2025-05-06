import fitz  # PyMuPDF
from transformers import BartForConditionalGeneration, BartTokenizer
import sys
import json
import os
import time
import logging

# Configuration des logs pour écrire uniquement dans un fichier
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('pdf_summarizer.log')
    ]
)

class PDFSummarizer:
    def __init__(self):
        logging.info("Initialisation du modèle BART...")
        start_time = time.time()
        self.tokenizer = BartTokenizer.from_pretrained("facebook/bart-large-cnn")
        self.model = BartForConditionalGeneration.from_pretrained("facebook/bart-large-cnn")
        logging.info(f"Modèle BART initialisé en {time.time() - start_time:.2f} secondes")

    def extract_text_from_pdf(self, pdf_path):
        logging.info(f"Extraction du texte du PDF: {pdf_path}")
        start_time = time.time()
        doc = fitz.open(pdf_path)
        text = "\n".join(page.get_text("text") for page in doc)
        logging.info(f"Texte extrait en {time.time() - start_time:.2f} secondes")
        logging.info(f"Nombre de pages: {len(doc)}")
        logging.info(f"Nombre de caractères extraits: {len(text)}")
        return text

    def count_words(self, text):
        word_count = len(text.split())
        logging.info(f"Nombre de mots dans le texte: {word_count}")
        return word_count

    def estimate_tokens_from_words(self, word_count):
        tokens = int(word_count * 1.33)
        logging.info(f"Nombre estimé de tokens: {tokens}")
        return tokens

    def summarize_french(self, text):
        logging.info("Début de la génération du résumé...")
        start_time = time.time()
        
        word_count = self.count_words(text)
        target_word_count = word_count // 3
        max_length_tokens = self.estimate_tokens_from_words(target_word_count)
        max_length_tokens = min(max_length_tokens, 1024)
        
        logging.info(f"Paramètres de génération - max_length: {max_length_tokens}, min_length: 50")

        logging.info("Tokenization du texte...")
        tokenization_start = time.time()
        inputs = self.tokenizer(text, return_tensors="pt", max_length=1024, truncation=True)
        logging.info(f"Tokenization terminée en {time.time() - tokenization_start:.2f} secondes")

        logging.info("Génération du résumé...")
        generation_start = time.time()
        summary_ids = self.model.generate(
            inputs["input_ids"],
            max_length=max_length_tokens,
            min_length=50,
            length_penalty=2.0,
            num_beams=4,
            early_stopping=True
        )
        logging.info(f"Génération terminée en {time.time() - generation_start:.2f} secondes")

        logging.info("Décodage du résumé...")
        summary = self.tokenizer.decode(summary_ids[0], skip_special_tokens=True)
        logging.info(f"Résumé généré en {time.time() - start_time:.2f} secondes")
        logging.info(f"Longueur du résumé: {len(summary)} caractères")
        
        return summary

    def summarize_pdf(self, pdf_path):
        try:
            if not os.path.exists(pdf_path):
                logging.error(f"Fichier non trouvé: {pdf_path}")
                return json.dumps({"error": "Fichier non trouvé"})
            
            logging.info(f"Début du traitement du PDF: {pdf_path}")
            start_time = time.time()
            
            extracted_text = self.extract_text_from_pdf(pdf_path)
            french_summary = self.summarize_french(extracted_text)
            
            total_time = time.time() - start_time
            logging.info(f"Traitement complet terminé en {total_time:.2f} secondes")
            
            return json.dumps({"summary": french_summary})
        except Exception as e:
            logging.error(f"Erreur lors du traitement: {str(e)}", exc_info=True)
            return json.dumps({"error": str(e)})

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print(json.dumps({"error": "Usage: python pdf_summarizer.py <pdf_path>"}))
        sys.exit(1)

    pdf_path = sys.argv[1]
    logging.info(f"Lancement du script avec le fichier: {pdf_path}")
    summarizer = PDFSummarizer()
    result = summarizer.summarize_pdf(pdf_path)
    sys.stderr.flush()  # Vider le buffer stderr avant d'imprimer le JSON
    print(result) 